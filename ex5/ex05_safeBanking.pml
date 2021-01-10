ltl {
  (Semaphore == 2 -> <>(Semaphore == 1))
  (Semaphore == 1 -> <>(Semaphore == 0))
}

int Semaphore = 2; int Account = 0;

active [2] proctype Spouse(){ 
    int i = 0;
    do
    :: i >= 10 -> break;
    :: else -> Account++; i++;
    od;
    Semaphore--;
}

active proctype main(){ 
    Semaphore == 0;
    printf("Account = %d\n", Account);
    assert(Account == 20)
}
