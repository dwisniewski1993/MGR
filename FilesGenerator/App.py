import csv
import random

'''
Generate MGR dataset for neural network and Genetic Algoritm
'''


def generate_dataset(x_size, y_size, set_size):
    X = []
    Y = []
    for _ in range(set_size):
        x = [int(random.randint(0, 1)) for _ in range(x_size)]
        y = [int(random.randint(0, 1)) for _ in range(y_size)]

        X.append(x)
        Y.append(y)

    with open("X.csv", 'w', newline='') as myfile:
        for each in X:
            wr = csv.writer(myfile)
            wr.writerow(each)
    with open("Y.csv", 'w', newline='') as myfile:
        for each in Y:
            wr = csv.writer(myfile)
            wr.writerow(each)


def generate_semantic():
    file = open("semantic.xml", "w")

    file.write(r'<?xml version="1.0" encoding="UTF-8"?>'+'\n')
    file.write(r'<UNITS_LIST>'+'\n')

    for each in range(0, 10):
        currect = each
        file.write('\t'+'<UNIT id = "'+str(currect)+'">'+'\n')
        file.write('\t'+'\t'+r'<DIFFICULTY_LEVEL>'+str(random.randint(1, 5))+r'</DIFFICULTY_LEVEL>'+'\n')
        file.write('\t'+'\t'+r'<DISTANCE>'+'\n')
        for each in range(0, 10):
            if each != currect:
                file.write('\t'+'\t'+'\t'+r'<FROM id = "'+str(each)+'">'+str(random.randint(1, 5))+r'</FROM>'+'\n')
        file.write('\t'+'\t'+r'</DISTANCE>'+'\n')
        file.write('\t'+r'</UNIT>')

    file.write(r'</UNITS_LIST>')


def main():
    print("Generate NN dataset...")

    SIZE_X = 10
    SIZE_Y = 10
    SIZE_SET = 100
    generate_dataset(x_size=SIZE_X, y_size=SIZE_Y, set_size=SIZE_SET)
    print("DONE!")

    print("Generate NeuroGen.GA dataset...")
    generate_semantic()
    print("DONE!")


if __name__ == '__main__':
    main()
