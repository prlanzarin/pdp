#include <stdio.h>
#include <stdlib.h>
#include <math.h>
/* função que decide se n é primo, sendo n maior do que 1
retorna 1 caso o número seja primo, e 0 caso não seja */
int isprime(int n)
{
	int i, squareroot;
	if (n > 1) {
                squareroot = (int) sqrt((double) n);
		for (i = 3; i <= squareroot; i = i + 2) {
			if ((n % i) == 0) {
				return 0;
                        }
                }
		return 1;
        } else {
		return 0;
        }
}

int main(int argc, char **argv)
{
	int n,                  /* numero a ser testado */
         pc,                    /* prime counter */
         limit;                 /* maior número a ser testado */
	if (argc < 2) {
            printf("Digite o numero a ser testado\n");
	return 0;
        } else {
            sscanf(argv[1], "%d", &limit);
        }
        printf("\nEncontrar numeros primos entre 2 e %d (inclusive).\nCalculando...", limit);
        pc = 0;                 /* Inicializa o contador de primos */
	for (n = 1; n <= limit; n = n + 2) {  /* testa sequencialmente se um 
número é primo ou nao */
		if (isprime(n)) {
                        pc++;
                }
        }
        printf("\nFinalizado.\nTotal de primos encontrados %d\n", pc);
	return 0;
}