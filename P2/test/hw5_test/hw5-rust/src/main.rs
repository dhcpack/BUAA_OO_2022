#![feature(drain_filter)]

mod elevator_input;
mod thread_elevator;
mod timed_output;

use std::{collections::HashMap, io, sync::mpsc, thread};

use elevator_input::ElevatorInput;
use thread_elevator::ThreadElevator;
use timed_output::TimedOutput;

fn range_incl(a: u32, b: u32) -> Box<dyn Iterator<Item = u32>> {
    if a > b {
        Box::new((b..=a).rev())
    } else {
        Box::new(a..=b)
    }
}

fn main() {
    let output = TimedOutput::new();
    let stdin = io::stdin();

    let mut handles = vec![];
    let mut senders = HashMap::new();

    for (idx, building) in ['A', 'B', 'C', 'D', 'E'].into_iter().enumerate() {
        let (sender, recv) = mpsc::channel();
        let mut elevator = ThreadElevator::new(
            //
            recv,
            output.clone(),
            building,
            idx as u32 + 1,
        );
        let handle = thread::spawn(move || elevator.run());

        handles.push(handle);
        senders.insert(building, sender);
    }

    for req in ElevatorInput::new(stdin.lock()) {
        senders[&req.building].send(req).unwrap();
    }

    drop(senders);
    for handle in handles {
        handle.join().unwrap();
    }
}
