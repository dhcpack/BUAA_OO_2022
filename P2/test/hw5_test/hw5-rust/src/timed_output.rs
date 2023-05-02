use std::{
    sync::{Arc, Mutex},
    time::Instant,
};

pub struct TimedOutput {
    inner: Arc<Mutex<Instant>>,
}

impl TimedOutput {
    pub fn new() -> Self {
        Self {
            inner: Arc::new(Mutex::new(Instant::now())),
        }
    }

    pub fn println(&self, s: &str) {
        let time = self.inner.lock().unwrap();
        println!("[{:8.4}]{}", time.elapsed().as_secs_f32(), s);
    }

    pub fn arrive(&self, building: char, floor: u32, e_id: u32) {
        self.println(&format!("ARRIVE-{}-{}-{}", building, floor, e_id));
    }

    pub fn open(&self, building: char, floor: u32, e_id: u32) {
        self.println(&format!("OPEN-{}-{}-{}", building, floor, e_id));
    }

    pub fn close(&self, building: char, floor: u32, e_id: u32) {
        self.println(&format!("CLOSE-{}-{}-{}", building, floor, e_id));
    }

    pub fn p_out(&self, p_id: u32, building: char, floor: u32, e_id: u32) {
        self.println(&format!("OUT-{}-{}-{}-{}", p_id, building, floor, e_id));
    }

    pub fn p_in(&self, p_id: u32, building: char, floor: u32, e_id: u32) {
        self.println(&format!("IN-{}-{}-{}-{}", p_id, building, floor, e_id));
    }
}

impl Clone for TimedOutput {
    fn clone(&self) -> Self {
        Self {
            inner: self.inner.clone(),
        }
    }
}
