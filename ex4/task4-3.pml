#define ROCK 0
#define SCISSORS 1
#define PAPER 2
int Semaphore = 2
int p1 = 0;
int p2 = 0;

active proctype P1() {
  int rand = 2
  if
    :: rand == 2 -> p1 = ROCK
    :: rand > 0 -> p1 = SCISSORS
    :: rand % 2 == 0 -> p1 = PAPER
  fi
  Semaphore--
}

active proctype P2() {
  int rand = 2
  if
    :: rand == 2 -> p2 = ROCK
    :: rand > 0 -> p2 = SCISSORS
    :: rand % 2 == 0 -> p2 = PAPER
  fi
  Semaphore--
}

active proctype main() {
  Semaphore == 0
  int result = (p1 - p2 + 3) % 3
  if
    :: result == 0 -> printf("draw\n")
    :: result == 1 -> printf("second won\n")
    :: result == 2 -> printf("first won\n")
  fi
}
