import cv2


def getFace(img):
    cascPath = r"haarcascade_frontalface_default.xml"
    faceCascade = cv2.CascadeClassifier(cascPath)

    #img = cv2.resize(img, (img.shape[1] // 8, img.shape[0] // 8))

    #if img.shape[1] > img.shape[0]:
    #    img = cv2.rotate(img, cv2.ROTATE_90_COUNTERCLOCKWISE)

    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(30, 30),
        flags=cv2.CASCADE_SCALE_IMAGE
    )

    # Draw a rectangle around the faces
    # for (x, y, w, h) in faces:
    #    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)

    faceFrame = None

    # če najde obraz ga prikazuje na manjši slikci
    try:
        fx = faces[0][0]
        fy = faces[0][1]
        fw = faces[0][2]
        fh = faces[0][3]
        faceFrame = img[fy:fy + fh, fx:fx + fw]
        faceFrame = cv2.resize(faceFrame, (200, 200))
        # cv2.imshow("face", faceFrame)
        # cv2.imshow("input", img)
        # cv2.waitKey(0)
    except:
        print("No face detected")
        return None

    return faceFrame
