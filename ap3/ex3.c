#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <mpi.h>

/* tamanho do vetor */
int ID, TARGET, NOF_PROC, SIZE;

int main ( int argc, char **argv ){
        int occurrences = 0;   /* numero de ocorrencias do nmero alvo */
        int tot_oc = 0;
        int vec[SIZE];      /* vetor no qual o numero ser buscado */
        int i;                   /* iterador */
        MPI_Status status;
        MPI_Init(&argc, &argv);
        MPI_Comm_rank(MPI_COMM_WORLD, &ID);
        MPI_Comm_size(MPI_COMM_WORLD, &NOF_PROC);

        if(ID == 0) {
                srand(0);
                if (argc < 2) {
                        printf("Digite o numero a ser contado\n");
                        MPI_Finalize();
                        exit(0);
                } 
                else {
                        sscanf(argv[1], "%d", &TARGET);
                        sscanf(argv[2], "%d", &SIZE);
                }
                
                printf("PROCURANDO %d NO VETOR: ", TARGET);

                for(i=0; i<SIZE; i++){
                        vec[i] = rand() % 10;
                }

                for(i=0; i<SIZE; i++){
                        printf("%d ", vec[i]);
                }        
                printf("\n");
        }

        MPI_Bcast(&TARGET, 1, MPI_INTEGER, 0, MPI_COMM_WORLD);
        MPI_Bcast(&SIZE, 1, MPI_INTEGER, 0, MPI_COMM_WORLD);

        int *parc = (int *) malloc(SIZE/NOF_PROC*sizeof(int));

        MPI_Scatter(vec, SIZE/NOF_PROC, MPI_INT, parc, SIZE/NOF_PROC, MPI_INT, 0, MPI_COMM_WORLD);

        printf("PROCURANDO %d NO PROCESSO %d COM SUBVETOR:", TARGET, ID);

        for(i=0; i<SIZE/NOF_PROC; i++) {      /* busca sequencial pelo numero */
                        printf("%d ", parc[i]);
                }        
                printf("\n");

        for(i=0; i<SIZE/NOF_PROC; i++) {      /* busca sequencial pelo numero */
                if(parc[i] == TARGET) {   /* ocorrencia encontrada */
                        occurrences++;
                }
        }

        MPI_Reduce(&occurrences,&tot_oc,1,MPI_INT,MPI_SUM,0,MPI_COMM_WORLD);


        if(ID == 0)
                printf("\nFinalizado\n\nNumero de vezes em que %d aparece no vetor eh: %d\n", TARGET, tot_oc);

        MPI_Finalize();
        return 0;
}

