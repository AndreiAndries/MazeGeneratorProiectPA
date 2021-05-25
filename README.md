# MazeGeneratorProiectPA - Andrtieș Dumitru-Andrei grupa B1

Maze Generator in Java

Am ales sa fac acest proiect pentru a-mi putea îmbunătăți cunoștințele și skill-urile legate de algoritmică!

Proiectul conține următoarele clase:

-Main

-Confirm Box

-GrowingTreeNewest

-GrowingTreeOldest

-GrowingTreeRandom

-HuntAndKillMazeGenerator

-KruskalMazeGenerator

-MazeGenerator

-MazeGenerator3D

-MazeGeneratorPerfect

In clasa main se creeaza meniul pentru a genera diferite labirinturi 2D sau 3D.

![image](https://user-images.githubusercontent.com/75743080/119485607-f4b55780-bd5f-11eb-928e-14a8fe191f4f.png)

La meniul de sus se alege algoritmul,dimensiunea si marimea labirintului care se doreste a fi creeat.

![image](https://user-images.githubusercontent.com/75743080/119485957-5ecdfc80-bd60-11eb-919a-479b0ef563a6.png)
![image](https://user-images.githubusercontent.com/75743080/119485987-655c7400-bd60-11eb-8d56-b7071d979b76.png)
![image](https://user-images.githubusercontent.com/75743080/119486011-6beaeb80-bd60-11eb-90de-f5c5375d71ae.png)

Butonul close inchide programul de tot insa mereu va apărea un mesaj daca esti sigur vrei sa inchizi acest program de asemenea acest mesaj apare si daca inchidem din X programul.

![image](https://user-images.githubusercontent.com/75743080/119486336-d00daf80-bd60-11eb-9c3b-01ca79b7e560.png)

Butonul Settings ne duce intr-o alta scena ce modifica vizual modul in care se poate genera labirintul!

![image](https://user-images.githubusercontent.com/75743080/119486569-1105c400-bd61-11eb-9ad6-0c0494310c6c.png)

Dupa ce se apasa butonul :Set: voi fi setate obtiunile alese de utilizator si se va reveni la meniul initial.

Pentru Labirinturi 2D sunt implementati urmatorii algoritmi.

![image](https://user-images.githubusercontent.com/75743080/119486884-66da6c00-bd61-11eb-83c1-012bbeb82e04.png)

Ce genereaza in mod diferit zabirinturile,iar pentru Labirinturile 3D sunt doua modalitati diferite de a implementa un algoritm.

Primul mod este de a genera cu recursive Backtracker cele doua nivele ale labirintului iar scarile se vor pune dupa ce cele doua nivele sunt generate! Acest mod este modul mai stupid de a genera un labirint 3D.

![image](https://user-images.githubusercontent.com/75743080/119487356-f1bb6680-bd61-11eb-9390-bc82e3f595af.png)
![image](https://user-images.githubusercontent.com/75743080/119487382-f97b0b00-bd61-11eb-9737-c18be7d62f87.png)

Al doilea mod este modul inteliogent de a genera un labirint 3D deoarece se construieste de la 0 si se verifica daca poate sa mearga in orice directie dintre N,S,E,V și un nivel mai sus/jos astfel modul de rezolvare rezolvare al rabirintului este mai greu.

![image](https://user-images.githubusercontent.com/75743080/119487739-6bebeb00-bd62-11eb-8b56-87d0c06936b2.png)
![image](https://user-images.githubusercontent.com/75743080/119487767-727a6280-bd62-11eb-8b2b-cbbe2848f27c.png)

Pentru a accesa modul inteligent de generare a labirintului 3D in meniul principal in casuta de jos trebuie scris "yes" si apasat butonul :Print Data: iar sus in meniu la dimension trebuie selectat 3D.

![image](https://user-images.githubusercontent.com/75743080/119488084-d0a74580-bd62-11eb-9ac4-272a17d7bbe8.png)

Toate labirintele 2D au generate solve-ruri ce rezolva labirintul punctul de plecare fiind celula din stanga sus iar punctul de sosire fiind celula din drapta jos.

![image](https://user-images.githubusercontent.com/75743080/119488437-385d9080-bd63-11eb-934e-45022fe6b0df.png)
![image](https://user-images.githubusercontent.com/75743080/119488542-5925e600-bd63-11eb-8952-90811f360432.png)
![image](https://user-images.githubusercontent.com/75743080/119488634-75c21e00-bd63-11eb-8065-7d028d17fd2f.png)

Pentru a regenera un labirint din acelasi ecran trebuie selectate viteza cu care se vrea a fi generat labirintul si trebuie apasat butonul de start.

![image](https://user-images.githubusercontent.com/75743080/119488841-ad30ca80-bd63-11eb-9903-3f4661ec3ccc.png)

Daca se vrea oprirea generarii algoritmului trebuie apasat butonul stop.

![image](https://user-images.githubusercontent.com/75743080/119488930-c5a0e500-bd63-11eb-9a14-546a78cd43ce.png)

Prntru a salva labirintul avem 3 formate in care acesta se poate salva : JPG, PNG și TXT; și trebuie neaparat sa apasam o data putonul stop pentru a trimite un semnal thread-ului de a se opri dupa care se poate apasa butonul :Save: .

![image](https://user-images.githubusercontent.com/75743080/119489355-47910e00-bd64-11eb-9f97-4b6830c64d80.png)

Daca se vrea salvarea in format TXT nu trebie introdus absolut nimic. Se va genera un fisier text in care se va salva labirintul.

In caz ca se vrea a fi salvat in format de imagine trebuie introdus un path cu directorul in care se vrea a fi salvata imaginea.

![image](https://user-images.githubusercontent.com/75743080/119489640-8f179a00-bd64-11eb-86f7-13960790d99c.png)

Pentru maze-urile 3D fiinca se vor genera doua poze nu va trebui sa adaugam numele care se vrea pentru fisier in director.
Dar in caz ca salvam un maze 2D trebuie introdus si numele fisierului in care se vrea a fi salvat.

Pentru partea de front-end am folosit JavaFX iar in momentul generarii maze-ului, fiecare algoritm are un thread asociat ce genereaza maze-ul.
Fiecare maze are in spate o matrice 2 sau 3 dimensionala pentru generarea labirintuli.

Pentru Detalii despre fiecare algoritm sunt scrise comentarii la inceputul fiecarei clase ce reprezinta un algoritm de generare.

# Surse de inspiratie:

- https://www.youtube.com/watch?v=sVcB8vUFlmU

- https://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm

- https://en.m.wikipedia.org/wiki/Maze_generation_algorithm

# Locul de unde am invatat JavaFX:
- https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
