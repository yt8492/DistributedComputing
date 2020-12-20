#define N 5
int i = 0
int A[N]

active [N] proctype worker() {
  atomic {
    A[i] = _pid
    i++
  }
}

active proctype main() {
  i == N
  i = 0
  do
  :: i >= N -> break
  :: else -> printf("%d\n", A[i])
             i++
  od
}
