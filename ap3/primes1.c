#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>
#include <math.h>
/* funcao que decIDe se n eh primo, sendo n maior do que 1
   retorna 1 caso o n�mero seja primo, e 0 caso nao seja */
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

int isAble(int nof_proc, int lim) {
        if (((nof_proc % 2) != 0) || ((lim % nof_proc) !=0)) {
                printf("O num de procs %d precisa ser par e divisivel pelo limite %d!\n", nof_proc, lim);
                MPI_Finalize();
                exit(0);
        }
}


int ID, NOF_PROC;
int limit;

int main(int argc, char **argv)
{
        MPI_Status status;
        MPI_Init(&argc, &argv);
        MPI_Comm_rank(MPI_COMM_WORLD, &ID);
        MPI_Comm_size(MPI_COMM_WORLD, &NOF_PROC);

        int st_pt = (ID*2)+1;
        int slice = NOF_PROC*2;
        int pc = 0;
        int found =0; int i = 0; int p = 0;
        if(ID == 0) {
                if (argc < 2) {
                        printf("Digite o numero a ser testado\n");
                        MPI_Finalize();
                        exit(0);
                } 
                else 
                        sscanf(argv[1], "%d", &limit);

                isAble(NOF_PROC, limit); 

                for(p = 1; p < NOF_PROC; p++)
                        MPI_Send(&limit, 1, MPI_INTEGER, p, 100, MPI_COMM_WORLD);

                printf("\nEncontrar numeros primos entre 2 e %d (inclusive).\nCalculando...", limit);               

                pc = 0;
                for(i = st_pt; i <= limit; i += slice) {
                        if(isprime(i))
                                pc++;
                }
                int part_pc = 0;

                for(p = 1; p < NOF_PROC; p++) {
                        MPI_Recv(&part_pc, 1, MPI_INTEGER, p, 200, MPI_COMM_WORLD, &status);
                        pc += part_pc; 
                }

                printf("\nFinalizado.\nTotal de primos encontrados %d\n", pc);        

        }
        else {
                MPI_Recv(&limit, 1, MPI_INTEGER, 0, 100, MPI_COMM_WORLD, &status);
                isAble(NOF_PROC, limit);

                for(i = st_pt; i <= limit; i += slice) {
                        if(isprime(i))
                                pc++;
                }

                MPI_Send(&pc, 1, MPI_INTEGER, 0, 200, MPI_COMM_WORLD);
        }

        MPI_Finalize();
        exit(0);

}
