use regex::Regex;
use std::{fmt::Debug, io::BufRead};

#[derive(Copy, Clone)]
pub struct PersonRequest {
    pub person_id: u32,
    pub building: char,
    pub fr_floor: u32,
    pub to_floor: u32,
}

pub struct ElevatorInput<T>
where
    T: BufRead,
{
    inner: T,
    line: String,
    re: Regex,
}

const INPUT_FORMAT: &str = r"(\d+)-FROM-([A-Z])-(\d+)-TO-([A-Z])-(\d+)";

impl Debug for PersonRequest {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(
            f,
            "<PR: {} {} {} {}>",
            self.person_id, self.building, self.fr_floor, self.to_floor
        )
    }
}

impl<T> ElevatorInput<T>
where
    T: BufRead,
{
    pub fn new(inner: T) -> Self {
        Self {
            inner,
            line: String::new(),
            re: Regex::new(INPUT_FORMAT).unwrap(),
        }
    }
}

impl<T> Iterator for ElevatorInput<T>
where
    T: BufRead,
{
    type Item = PersonRequest;

    fn next(&mut self) -> Option<Self::Item> {
        loop {
            self.line.clear();
            // stop iteration when EOF or Error
            if self.inner.read_line(&mut self.line).ok()? == 0 {
                return None;
            }
            // match the line with regex
            if let Some(caps) = self.re.captures(&self.line) {
                if caps[2] != caps[4] {
                    continue;
                }
                return Some(PersonRequest {
                    person_id: caps[1].parse().unwrap(),
                    building: caps[2].chars().next().unwrap(),
                    fr_floor: caps[3].parse().unwrap(),
                    to_floor: caps[5].parse().unwrap(),
                });
            }
        }
    }
}
