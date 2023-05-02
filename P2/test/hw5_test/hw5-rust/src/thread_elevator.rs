use std::{cmp::Ordering, sync::mpsc::Receiver, thread, time::Duration};

use crate::{elevator_input::PersonRequest, range_incl, timed_output::TimedOutput};

pub struct ThreadElevator {
    recv: Receiver<PersonRequest>,
    output: TimedOutput,
    pending: Vec<PersonRequest>,
    people: Vec<PersonRequest>,
    floor: u32,
    building: char,
    id: u32,
}

const MAX_PEOPLE: usize = 6;
const DUR_DOOR: Duration = Duration::from_millis(400);
const DUR_FLOOR: Duration = Duration::from_millis(400);

impl ThreadElevator {
    pub fn new(
        recv: Receiver<PersonRequest>,
        output: TimedOutput,
        building: char,
        id: u32,
    ) -> Self {
        Self {
            recv,
            output,
            pending: vec![],
            people: vec![],
            floor: 1,
            building,
            id,
        }
    }

    fn update_pending(&mut self) {
        self.pending.extend(self.recv.try_iter().map(|ppl| {
            // self.output.println(&format!("DEBUG: got {:?}", ppl));
            ppl
        }));
    }

    fn wait_if_empty(&mut self) -> Option<()> {
        if self.pending.is_empty() {
            self.pending.push(self.recv.recv().ok()?);
        }
        self.update_pending();

        if self.pending.is_empty() {
            None
        } else {
            Some(())
        }
    }

    fn drop_off(&mut self) -> Vec<PersonRequest> {
        self.people
            .drain_filter(|ppl| ppl.to_floor == self.floor)
            .collect()
    }

    fn take_on(&mut self, dir: Ordering) -> Vec<PersonRequest> {
        self.update_pending();
        self.pending
            .drain_filter(|ppl| {
                if ppl.fr_floor != self.floor {
                    return false;
                }
                if dir != Ordering::Equal && dir != ppl.fr_floor.cmp(&ppl.to_floor) {
                    return false;
                }
                if self.people.len() >= MAX_PEOPLE {
                    return false;
                }
                self.people.push(*ppl);
                return true;
            })
            .collect()
    }

    fn at_floor(&mut self, dir: Ordering) {
        let drop = self.drop_off();
        let take = self.take_on(dir);
        if drop.is_empty() && take.is_empty() {
            return;
        }
        self.output.open(self.building, self.floor, self.id);
        for ppl in drop {
            self.output
                .p_out(ppl.person_id, self.building, self.floor, self.id);
        }
        for ppl in take {
            self.output
                .p_in(ppl.person_id, self.building, self.floor, self.id);
        }
        thread::sleep(DUR_DOOR);
        self.output.close(self.building, self.floor, self.id);
    }

    fn move_to(&mut self, target: u32) {
        // self.output.println(&format!("DEBUG: move_to {}", target));
        let fl_begin = self.floor;
        let move_dir = self.floor.cmp(&target);
        for floor in range_incl(fl_begin, target) {
            self.floor = floor;
            if self.floor != fl_begin {
                self.output.arrive(self.building, self.floor, self.id);
            }
            self.at_floor(move_dir);
            if self.floor != target {
                thread::sleep(DUR_FLOOR);
            }
        }
    }

    pub fn run(&mut self) {
        while let Some(_) = self.wait_if_empty() {
            loop {
                // als: find the target
                let target = if self.people.is_empty() {
                    self.update_pending();
                    match self.pending.get(0) {
                        Some(req) => req.fr_floor,
                        None => break,
                    }
                } else {
                    self.people[0].to_floor
                };
                // als: move to the target
                self.move_to(target);
            }
        }
    }
}
