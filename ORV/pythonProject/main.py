import cv2
import numpy as np
import math


# import tensorflow as tf


# histogram za sivinsko sliko
def Histogram(img):
    histogram = np.zeros(256)

    for i in range(img.shape[0]):
        for j in range(img.shape[1]):
            bin = img[i, j]
            histogram[bin] = histogram[bin] + 1

    return histogram


# LBP 8,1 (8sosedov razdalje 1)
def LBP(img):
    img = img.copy()
    imgLBP = np.zeros_like(img)

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    binary = np.zeros(8, dtype=int)

    # vrstica
    for i in range(img.shape[0] - 2):
        # stolpec
        for j in range(img.shape[1] - 2):
            a = (img[i:i + 3, j:j + 3])
            # print(a)
            count = 0
            # maska vrstica
            for k in range(a.shape[0]):
                # vaska stolpec
                for l in range(a.shape[1]):
                    # ignorira center
                    if k == 1 and l == 1:
                        pass
                    # primerja s centrom maske
                    else:
                        if a[k, l] >= a[1, 1]:
                            binary[count] = 1
                        else:
                            binary[count] = 0
                        count = count + 1
                        # print(a[k, l], k, l, i, j)
            # print(binary)

            # številka, ki jo bobiš od LBG (binary to decimal)
            number = 0
            for m in range(8):
                number = number + binary[m] * 2 ** np.absolute(m - 7)

            # print(number)
            imgLBP[i, j] = number

    # print(img)
    result = Histogram(imgLBP)
    result = result.astype(int)

    return result, imgLBP


def HOG(img):
    img = img.copy()

    # hardcoded
    cellSize = 8
    bins = 9
    blockSize = 2

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    Gx = cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize=5)
    Gy = cv2.Sobel(img, cv2.CV_64F, 0, 1, ksize=5)

    # gradienti in koti (v rad)
    G = np.sqrt(np.square(Gx) + np.square(Gy))
    angles = np.arctan2(Gx, Gy)

    # vse kote protvori iz rad v stopinje in spravi v 0-180 interval
    for i in range(angles.shape[0]):
        for j in range(angles.shape[1]):
            angles[i, j] = math.degrees(angles[i, j])
            if angles[i, j] < 0:
                angles[i, j] = angles[i, j] + 180

    # razdeli sliko v 8x8 (cellSize) polja
    # https://stackoverflow.com/questions/5953373/how-to-split-image-into-multiple-pieces-in-python
    # prvi odgovor
    cells = [img[x:x + cellSize, y:y + cellSize] for x in range(0, img.shape[0], cellSize) for y in
             range(0, img.shape[1], cellSize)]

    cellsGradients = [G[x:x + cellSize, y:y + cellSize] for x in range(0, G.shape[0], cellSize) for y in
                      range(0, G.shape[1], cellSize)]

    cellsAngles = [angles[x:x + cellSize, y:y + cellSize] for x in range(0, angles.shape[0], cellSize) for y in
                   range(0, angles.shape[1], cellSize)]

    step_size = 180 // bins
    array = np.zeros(bins)

    cellArrays = []

    # za vsako celico nafila tisto polje iz matrike kotov in gradientov
    for c in range(len(cells)):
        # čez matriko celice
        for i in range(cellsGradients[0].shape[0]):
            for j in range(cellsGradients[0].shape[1]):
                # perfektna delitev
                if cellsAngles[c][i, j] % step_size == 0:
                    index = int(cellsAngles[c][i, j] / step_size)
                    # kot 180 je isto kot 0
                    index = index % 9
                    array[index] = array[index] + cellsGradients[c][i, j]
                else:
                    index = int(cellsAngles[c][i, j] / step_size)
                    val = (index + 1) * step_size

                    leftBias = (val - cellsAngles[c][i, j]) / step_size
                    rightBias = 1 - leftBias

                    # left
                    array[index] = array[index] + cellsGradients[c][i, j] * leftBias
                    # right (če kot < 160 je al zadni al prvi)
                    if index + 1 == 9:
                        array[0] = array[0] + cellsGradients[c][i, j] * rightBias
                    else:
                        array[index + 1] = array[index + 1] + cellsGradients[c][i, j] * rightBias

        cellArrays.append(array)
        array = np.zeros(bins)

    # matrika pove, katere celice spadajo v blok
    blockCellsIndexes = blockCellIndexes(img, cellSize, blockSize)

    # blocksInRow = (img.shape[0] // cellSize) - 1
    # blocksInColumn = (img.shape[1] // cellSize) - 1

    # polja celic se združijo v polje bloka
    blockArrays = []
    blockArray = None
    for i in range(blockCellsIndexes.shape[0]):
        for j in range(0, blockCellsIndexes.shape[1]):
            if j == 0:
                blockArray = cellArrays[int(blockCellsIndexes[i, 0])]
            else:
                a = cellArrays[int(blockCellsIndexes[i, j])]
                blockArray = np.concatenate((blockArray, a))

        blockArrays.append(blockArray)

    # polja blokov se normalizirajo
    for i in range(len(blockArrays)):
        k = np.sqrt(np.sum(blockArrays[i] ** 2))
        for j in range(blockArrays[0].shape[0]):
            blockArrays[i][j] = blockArrays[i][j] / k

    # iz 2d polja v 1d
    result = np.array(blockArrays).flatten()

    return result


# hardcoded za 8x8 cellSize in 2x2 block size
def blockCellIndexes(img, cellSize, blockSize):
    blocksInRow = (img.shape[0] // cellSize) - 1
    blocksInColumn = (img.shape[1] // cellSize) - 1

    blockCellsIndexes = np.zeros((blocksInRow * blocksInColumn, 4))
    blockCellsIndexes[0, 0] = 0
    blockCellsIndexes[0, 1] = 1
    blockCellsIndexes[0, 2] = (img.shape[0] / cellSize)
    blockCellsIndexes[0, 3] = (img.shape[0] / cellSize) + 1

    for i in range(1, blockCellsIndexes.shape[0]):
        for j in range(blockCellsIndexes.shape[1]):
            if i % blocksInRow == 0:
                blockCellsIndexes[i, j] = blockCellsIndexes[i - 1, j] + 2
            else:
                blockCellsIndexes[i, j] = blockCellsIndexes[i - 1, j] + 1

    return blockCellsIndexes


def mergeLBPandHOG(LBP, HOG):
    return np.concatenate((LBP, HOG))


img = cv2.imread(r'C:\Users\janpu\Pictures\muc.jpg')
img = cv2.resize(img, (200, 200))
testLBP, test = LBP(img)
# histogram = cv2.calcHist(test, [0], None, [256], [0, 256])
cv2.waitKey(0)

testHOG = HOG(img)

testLBPHOG = mergeLBPandHOG(testLBP, testHOG)

cv2.destroyAllWindows()
