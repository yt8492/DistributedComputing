int stick1 = 1
int stick2 = 1
int Semaphore = 2
active [2] proctype P1() {
  atomic {
    stick1 > 0
    stick1--
  }
  atomic {
    stick2 > 0
    stick2--
  }
  stick1++;
  stick2++;
  Semaphore--
}

active proctype main() {
  Semaphore == 0
  assert(stick1 == 1)
  assert(stick2 == 1)
}

