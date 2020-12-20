#define N 5

int A[N]
int S1 = 1
int S2 = 0
int max = 0

active [N] proctype process() {
  S1 == 0
  atomic {
    if
      :: max < A[_pid] -> max = A[_pid]
      :: else -> 
    fi
    S2++
  }
}

active proctype prepare() {
  int i = 0
  do
    :: i == N -> break
    :: else  -> int r
      if
        :: r = 1
        :: r = 2
        :: r = 3
        :: r = 4
        :: r = 5
        :: r = 6
        :: r = 7
        :: r = 8
        :: r = 9
        :: r = 10
        :: else -> r = 0
      fi
      A[i] = r
      i++
  od
  max = A[0]
  S1 = 0
}

active proctype main() {
  S2 == N
  printf("max: %d\n", max)
}

