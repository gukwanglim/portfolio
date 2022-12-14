# 2 - mouse

# 12_left - Previous_page
# 12_right - Next_page
# 23 - click

# 125 - PrintScreen
# 345 - escape_show
# 123- show_from_current  이상함

# 12345 - show_from_begin

#mediapipe hand
import cv2
import mediapipe as mp
import pyautogui
import math
import time
import numpy as np
import math

from flask import Flask

app = Flask(__name__)

TIME_Previous_page, TIME_Next_page  = 0, 0 #너무 민감하지 않고 연속으로 너무 많이 실행하지 않고 이전 페이지로 점프하고 다음 페이지로 점프하는 것을 방지하는 데 사용됩니다.。
delay_Previous_page, delay_Next_page = 1.5, 1.5  #너무 민감하지 않고 연속으로 너무 많이 실행하지 않고 이전 페이지로 점프하고 다음 페이지로 점프하는 것을 방지하는 데 사용됩니다. 2초 간격을 두어
TIME_PrintScreen = 0 #스크린샷이 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다.
delay_PrintScreen = 2
TIME_escape_show = 0 # 너무 민감하기 때문에 연속으로 너무 많이 실행하지 않도록하십시오.
delay_escape_show = 2
 #스크린샷이 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다. 2초 간격을 두어
TIME_show_from_begin, TIME_show_from_current = 0 , 0  #복사-붙여넣기가 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다.
delay_show_from_begin, delay_show_from_current= 3 , 3 #너무 민감해서 복사 붙여넣기가 연속으로 너무 많이 실행되는 것을 방지하기 위해 사용합니다. 1초 간격을 두어

# 현재 시간으로 이름을 지정하고 반환합니다.
# image name = a1 + a2 + "_" + a3 + a4 + "_"
def name_img_by_current_time():
    A = time.ctime()
    A = A.split()

    a1='0'
    mon = {'Oct':'10', 'Nov':'11' , 'Dec':'12', 'Jan':'01' , 'Feb':'02', 'Mar':'03' , 'Apr':'04', 'May':'05' , 'Jan':'06', 'Jul':'07' , 'Aug':'08', 'Sep':'09'}

    for (key, value) in mon.items():
        if A[1]==key:
            a1=value

    a2, a3, a4 = A[2], A[3][0:2], A[3][3:5]

    img_name_head = a1 + a2 + "_" + a3 + a4 + "_"
    return img_name_head

#p1, p2는 두 노드의 인덱스입니다.
#r은 원의 반지름입니다. 중지와 검지 끝의 (x,y) 좌표는 원으로 그려야 하기 때문에
#(120,120,120)원의 색이다
def distance(p1, p2, img, keypoint_pos, r=10, t=3):
    x1, y1 = int(keypoint_pos[p1][0]), int(keypoint_pos[p1][1])
    x2, y2 = int(keypoint_pos[p2][0]), int(keypoint_pos[p2][1])
    cx, cy = int((x1 + x2) // 2), int((y1 + y2) // 2)

    #매개변수를 정수로 변환해야 함，否則line()、circle()오류를 보고할 것입니다。
    cv2.line(img, (x1, y1), (x2, y2), (0, 0, 255), t)
    cv2.circle(img, (x1, y1), r, (120, 120, 120), cv2.FILLED)
    cv2.circle(img, (x2, y2), r, (120, 120, 120), cv2.FILLED)
    cv2.circle(img, (cx, cy), r, (255, 0, 255), cv2.FILLED)

    #두 노드 사이의 연결 길이 계산
    l = int(math.hypot(x2 - x1, y2 - y1))

    return l, img, [cx, cy]

#순서에 따라 다른 이미지 처리 수행
def img_processing(image, order):
    if order ==0: 
        po_image = cv2.flip(image, 1) ##수평 뒤집기

    if order==1:
        po_image = cv2.flip(image, 0) ##수직으로 뒤집다

    if order==3: #rectangular_mask
        h, w, c = image.shape[0], image.shape[1], image.shape[2]
        w_mask = w/2
        image[:, 0:round(w_mask), :] = np.zeros((h, round(w_mask), c),np.uint8)
        po_image = image

    if order==4:  ## use gray level img  //onlY 2D, SO INVOKE ERROR    ValueError: could not broadcast input array from shape (255,255) into shape (255,255,3)  
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        ret, pppo_image = cv2.threshold(gray, 127, 255, cv2.THRESH_BINARY) ##이진화

        #use to correspond to spec. of API set_input_tensor, which needs 3D input, not 2D
        #SO I need to make gray level img into 3D, BUT LOOK SAME as 2D.
        po_image = np.zeros((pppo_image.shape[0], pppo_image.shape[1], 3))
        po_image[:,:,0] = pppo_image[:,:]
        po_image[:,:,1] = pppo_image[:,:]
        po_image[:,:,2] = pppo_image[:,:]

    if order==5: #set value(0, 255, 160, 80) in a manner + bilateralFilter blur to reduce Noise by  11 17 17
        h, w, c = image.shape[0], image.shape[1], image.shape[2]
        for k in range(0, c):
            for i in range(0, h, 3):
                for j in range(0, w, 3):
                    image[i, j, k] = 0
        for k in range(0, c):
            for i in range(0, h, 5):
                for j in range(0, w, 5):
                    image[i, j, k] = 255
        for k in range(0, c):
            for i in range(0, h, 4):
                for j in range(0, w, 7):
                    image[i, j, k] = 160
        for k in range(0, c):
            for i in range(0, h, 6):
                for j in range(0, w, 11):
                    image[i, j, k] = 80
        
        po_image = cv2.bilateralFilter(image, 11, 17, 17)  # smoothing filter

    if order==10:
        po_image = cv2.GaussianBlur(image, (5, 5), 0) #blur to reduce Noise

    if order==11:  #톤 올 화이트
        w, h, c = image.shape[0], image.shape[1], image.shape[2]
        for i in range(w):
            for j in range(h):
                for k in range(c):
                    image[i,j,k] = 255
        po_image = image
    if order==12:  #전부 검은 색
        w, h, c = image.shape[0], image.shape[1], image.shape[2]
        for i in range(w):
            for j in range(h):
                for k in range(c):
                    image[i,j,k] = 0
        po_image = image
    return po_image

#p1, p2는 두 노드의 인덱스입니다.
#r은 원의 반지름입니다。  중지와 검지 끝의 (x,y) 좌표는 원으로 그려야 하기 때문에
#(120,120,120)원의 색이다
def distance(p1, p2, img, keypoint_pos, r=10, t=3):
    x1, y1 = int(keypoint_pos[p1][0]), int(keypoint_pos[p1][1])
    x2, y2 = int(keypoint_pos[p2][0]), int(keypoint_pos[p2][1])
    cx, cy = int((x1 + x2) // 2), int((y1 + y2) // 2)

    #매개변수를 정수로 변환해야 함，그렇지 않으면line()、circle()오류를 보고합니다。
    cv2.line(img, (x1, y1), (x2, y2), (0, 0, 255), t)
    cv2.circle(img, (x1, y1), r, (120, 120, 120), cv2.FILLED)
    cv2.circle(img, (x2, y2), r, (120, 120, 120), cv2.FILLED)
    cv2.circle(img, (cx, cy), r, (255, 0, 255), cv2.FILLED)

    #두 노드 사이의 연결 길이 계산
    l = int(math.hypot(x2 - x1, y2 - y1))

    return l, img, [cx, cy]

#제스처 확대
def enlarge(img, left_up, right_down):
    new_img = img.copy()
    size_w = abs(left_up[0] - right_down[0])
    size_h = abs(left_up[1] - right_down[1])
    
    # 정사각형 면적을 구하다image
    img_rec = img[left_up[1]:right_down[1], left_up[0]:right_down[0]]
    try:
        # 이 영역 확대
        img_rec = cv2.resize(img_rec, (size_w*2, size_h*2), interpolation=cv2.INTER_CUBIC)
    except:
        return None
    
    center = [(left_up[0] + right_down[0])//2, (left_up[1] + right_down[1])//2] # [x, y]
    
    # 원본 이미지의 정사각형 영역을 확대된 이미지로 교체
    for i in range(center[1]-size_h, center[1]+size_h):
        for j in range(center[0]-size_w, center[0]+size_w):
            try:
                new_img[i][j] = img_rec[i - (center[1]-size_h)][j - (center[0]-size_w)]
            except:
                continue
    
    return new_img 

# 주어진 사각형에 대한 모자이크
#입력left_up, right_down둘 다tuple，어떤 두 경계점의 점이다(x,y)값。
#왼쪽 상단 경계 점의 y 값을 얻으려면，사용left_up[1]。  오른쪽 하단 경계 점의 y 값을 얻으려면，사용right_down[1]。
#왼쪽 상단 경계 점의 x 값을 얻으려면，사용left_up[0]。  오른쪽 하단 경계 점의 x 값을 얻으려면，사용right_down[0]。
#지침의 범위에 따라，그 범위를 모자이크로 만드십시오，그리고 모자이크와 함께 사진을 반환。
def mosaic(img, left_up, right_down):
    new_img = img.copy()

    # size이 모자이크 블록에서 각 작은 영역의 측면 길이를 나타냅니다.
    #각각의 작은 파티션은 길이와 너비입니다.(w,h)=(10,10)작은 광장。
    size = 10

    #왼쪽 상단과 오른쪽 하단의 y 값에 따라size간격을 위한 출발
    for i in range(left_up[1], right_down[1]-size-1, size):
        #좌상단과 우하단의 X값에 따라size간격을 위한 출발
        for j in range(left_up[0], right_down[0]-size-1, size):

            try:
                # 이 작은 영역의 각 픽셀에는 맨 왼쪽 상단 픽셀의 값이 지정됩니다.，모자이크를 하다。
                new_img[i:i + size, j:j + size] = img[i, j, :]
            except:
                pass

    return new_img

#vector_2d_angle입력은 두 가지 유형의tuple의(x,y)
#v1,v2유형은tuple
#v1의(x,y)，두 노드의"x 벡터 차이"、"Y 벡터 차이"。  v2도(x,y)，두 노드의"x 벡터 차이"、"y벡터 차이"
#
# v1,v2두 벡터 사이의 각도 알아냄
#vector_2d_angle하나를 반환0-180값 사이，즉, 두 입력 벡터 사이의 각도(도 단위로 표현)。

def vector_2d_angle(v1,v2): 
    v1_x=v1[0] #두 노드의 "x 벡터 차이"
    v1_y=v1[1] #두 노드의 "y 벡터 차이"
    v2_x=v2[0]
    v2_y=v2[1]

    try:
        angle_= math.degrees(math.acos((v1_x*v2_x + v1_y*v2_y)/(((v1_x**2 + v1_y**2)**0.5)*((v2_x**2 + v2_y**2)**0.5))))
    except:
        angle_ = 100000.
    return angle_

#입력의 길이21의 목록，21개 노드의 (x, y) 값에 대해 각각
#hand_in:   [2]....[4]엄지손가락 / [5]....[8]검지손가락 / [9]....[12]중지손가락 / [13]...[16]약지손가락 / [17]...[20]새끼손가락
#hand_[i]의index=노드
#hand_[i][0]의index=노드의 X 값，hand_[i][1]의index=노드의 Y 값
#
#엄지손가락을 포함하는 목록 반환、검지, 중지, 약지 및 새끼 손가락의 개별 각도
#각 손가락의 각도를 개별적으로 계산합니다. 각 손가락의 각도를 2차원 공간에서 각도로 취급할 수 있기 때문입니다.

def hand_angle(hand_):

    angle_list = []

    angle_ = vector_2d_angle(
        ((int(hand_[0][0])- int(hand_[2][0])),(int(hand_[0][1])-int(hand_[2][1]))),  #(x,y) = (x0-x2, y0-y2)
        ((int(hand_[3][0])- int(hand_[4][0])),(int(hand_[3][1])- int(hand_[4][1])))  #(x,y) = (x3-x4, y3-y4)
    )
    angle_list.append(angle_)

    #이 각도는 "node[0] and node[6]선을 연결하여 형성된 벡터 "and" node[7] and node[8]연결에 의해 형성된 벡터의 끼인각"
    angle_ = vector_2d_angle(
        ((int(hand_[0][0])-int(hand_[6][0])),(int(hand_[0][1])- int(hand_[6][1]))),
        ((int(hand_[7][0])- int(hand_[8][0])),(int(hand_[7][1])- int(hand_[8][1])))
    )
    angle_list.append(angle_)

    #이 각도는 "node[0] and node[10]선을 연결하여 형성된 벡터 "and" node[11] and node[12]연결에 의해 형성된 벡터의 끼인각"
    angle_ = vector_2d_angle(
        ((int(hand_[0][0])- int(hand_[10][0])),(int(hand_[0][1])- int(hand_[10][1]))),
        ((int(hand_[11][0])- int(hand_[12][0])),(int(hand_[11][1])- int(hand_[12][1])))
    )
    angle_list.append(angle_)

    #이 각도는 "node[0] and node[14]선을 연결하여 형성된 벡터 "and" node[15]\ and node[16]연결에 의해 형성된 벡터의 끼인각"
    angle_ = vector_2d_angle(
        ((int(hand_[0][0])- int(hand_[14][0])),(int(hand_[0][1])- int(hand_[14][1]))),
        ((int(hand_[15][0])- int(hand_[16][0])),(int(hand_[15][1])- int(hand_[16][1])))
    )
    angle_list.append(angle_)

    #이 각도는 "node[0] and node[18]선을 연결하여 형성된 벡터 "and" node[19] and node[20]연결에 의해 형성된 벡터의 끼인각"
    angle_ = vector_2d_angle(
        ((int(hand_[0][0])- int(hand_[18][0])),(int(hand_[0][1])- int(hand_[18][1]))),
        ((int(hand_[19][0])- int(hand_[20][0])),(int(hand_[19][1])- int(hand_[20][1])))
    )
    angle_list.append(angle_)

    return angle_list

# 입력 angle_list는 길이가 5이고 요소 값이 0에서 180 사이인 목록으로 다섯 손가락의 개별 각도를 포함합니다.
#angle_list: [0]-->엄지 / [1]-->검지 / [2]-->중지 / [3]-->약지 / [4]-->새끼
#
# 검지 손가락만 펼친 경우 "검지 손가락 위로!"라는 문자열을 반환합니다. 
# 제스처가 집게 손가락을 펼치지 않으면 없음을 반환합니다.

def hand_gesture(angle_list, keypoint_pos):

    gesture_str = None

    if 100000. not in angle_list:
        if (angle_list[0]>90) and (angle_list[1]>90) and (angle_list[2]>90) and (angle_list[3]>90) and (angle_list[4]>90):
            gesture_str = "0"                  

        if (angle_list[0]<30) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]>40) and keypoint_pos[0][1]>keypoint_pos[4][1]:
            gesture_str = "1"

        if (angle_list[0]<30) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]>40) and keypoint_pos[0][1]<keypoint_pos[4][1]:
            gesture_str = "1DOWN"

        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]>40):
            gesture_str = "2"                  

        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]>40):
            gesture_str = "3"

        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "4"

        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "5"


        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]>40) and keypoint_pos[4][0]>keypoint_pos[8][0]:
            gesture_str = "12_left"             

        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]>40) and keypoint_pos[4][0]<keypoint_pos[8][0]:
            gesture_str = "12_right"       
 
        if (angle_list[0]<30) and (angle_list[1]>40) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]>40):
            gesture_str = "13"
        
        if (angle_list[0]<30) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "15"
        
        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]>40):
            gesture_str = "23"
        
        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "25"
        
        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "34"

        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "35"
        
        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]<30):
            gesture_str = "45"


        if (angle_list[0]<15) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]>40):
            gesture_str = "123"         

        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]>30) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "125"               

        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "234"             
        
        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "235"
        
        if (angle_list[0]>40) and (angle_list[1]>40) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]<30):
            gesture_str = "345"       
        

        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]>40) and (angle_list[4]<30):
            gesture_str = "1235"

        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]<30):
            gesture_str = "1245"

        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]<30):
            gesture_str = "2345"
        
        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]<30):
            gesture_str = "12345"


        #더 어려운 제스처
        #14  24  124  1234
        #엄지 손가락, 약지 손가락
        if (angle_list[0]<30) and (angle_list[1]>40) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "14"
        
        #검지 손가락 약지 손가락
        if (angle_list[0]>40) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "24"
        
        #엄지 손가락, 검지 손가락, 약지 손가락
        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]>40) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "124"
        
        #엄지 손가락, 검지 손가락, 약지 손가락, 새끼 손가락
        if (angle_list[0]<30) and (angle_list[1]<30) and (angle_list[2]<30) and (angle_list[3]<30) and (angle_list[4]>40):
            gesture_str = "1234"

    return gesture_str

#손의 마우스 커서를 따라갑니다
def mouse_track(frame, keypoint_pos):
    length, frame, lineInfo = distance(8, 12,frame, keypoint_pos) #8과 12는 검지와 중지의index

    #두 손가락 끝 사이의 거리가 매우 가까울 때 마우스 제어가 수행됩니다.
    #화면에 녹색 점이 표시됩니다
    if length < 50:
        cv2.circle(frame, (lineInfo[0], lineInfo[1]),15, (0, 255, 0), cv2.FILLED)

        #mapping방법---------------------------------------------------------------
        #뷰포트가 왼쪽 상단 모서리에 있을 때 손가락 끝의 좌표 범위는 다음과 같습니다. x = 0 ~ 1240, y = 0 ~ 700
        #그리고 이 컴퓨터 화면의 길이와 너비 = 1920, 높이 = 1080
        #
        #따라서 이것을 원래 손가락 끝 좌표에서 화면의 마우스 좌표로 매핑하십시오.
        # X = 1920/1240 * x
        # Y = 1080/700 * y
        #--------------------------------------------------------------------------

        #좌표를 화면 중앙으로 이동:   
        # X2=X1  620  350------(DX,DY)=(+340,+190)------->960  540
        # (LX, RX) = (960-620, 960+620) = (340, 1580)
        # (LY, RY) = (540-350, 540+350) = (190, 890)
        #
        #중심을 이동한 후 이 작은 창의 테두리와 중심 좌표는 다음과 같습니다.
        # 왼쪽 위 (LX, LY) = (340, 190)   ----> (0, 0)   340-(960-340)*(340/620)=0   190-(540-190)*(190/350)=0
        # (CX, CY) = (960, 540)  ----> (960, 540)
        # 오른쪽 아래 (RX, RY) = (1580, 890)  -----> (1920, 1080)
        # 왼쪽 아래 (ldx, ldy) = (340, 890) ----> (0, 1080)
        # 오른쪽 위 (rux, ruy) = (1580, 190)  -----> (1920, 0)
        #
        #이 작은 창의 경계와 중심 좌표를 큰 창(컴퓨터 화면 크기)에 매핑하면 좌표는 다음과 같습니다.
        #작은 창 절반 너비-340=620，작은 창 절반 높이 540-190=350
        #큰 창 절반 너비1920/2=960，큰 창 절반 너비1080/2=540
        #작은 창(x,y)=(150,200)---센터로 이동---->(x,y)=(150+340, 200+190)=(490, 390)------큰 화면으로 확장----->(x,y)=(490*[], 390+)

        PLACE_INDEX_FINGER = ""  #큰 화면의 중심을 기준으로 손가락 끝의 중심이 움직이는 방향을 기록(좌상단, 우하단, 좌하단, 우상단)

        #검지 끝의 좌표는 창을 왼쪽 상단 모서리에 놓았을 때의 좌표입니다.
        IndexFinger_x, IndexFinger_y = keypoint_pos[8][0], keypoint_pos[8][1] #검지 끝의 x,y 좌표

        W_LITTLE , H_LITTLE = 1240 , 700  #FIXME: 실측한건데 카메라 사이즈에 맞게 바꿔야지!!
        W_BIG , H_BIG = pyautogui.size()  #이 컴퓨터 화면 크기
        half_W_LITTLE , half_H_LITTLE = W_LITTLE/2 , H_LITTLE/2  #작은 창 절반 너비 / 작은 창 절반 높이
        half_W_BIG , half_H_BIG = W_BIG/2 , H_BIG/2  #큰 창 절반 너비 / 큰 창 절반 높이
        LX , LY = (W_BIG/2 - W_LITTLE/2) , (H_BIG/2 - H_LITTLE/2)  #왼쪽 위
        RX , RY = (W_BIG/2 + W_LITTLE/2) , (H_BIG/2 + H_LITTLE/2)  #오른쪽 아래
        CX , CY = LX + W_LITTLE / 2 , LY + H_LITTLE / 2  #가운데

        L_BIG = int(math.hypot(W_BIG, H_BIG))  #큰 창의 대각선 길이
        L_LITTLE = int(math.hypot(W_LITTLE, H_LITTLE))  #작은 창의 대각선 길이
        RATIO = L_BIG/L_LITTLE #두 창의 대각선 비율

        #작은 화면의 좌표를 큰 화면의 중앙에 인쇄
        IndexFinger_x, IndexFinger_y = IndexFinger_x + LX , IndexFinger_y + LY

        #중심을 기준으로 어느 방향을 결정합니다(왼쪽 위, 오른쪽 아래, 왼쪽 아래, 오른쪽 위).
        if IndexFinger_x==CX and IndexFinger_y==CY:
            PLACE_INDEX_FINGER="middle" #정중앙
        else:  #중앙에 없으면
            if IndexFinger_x>CX:
                if IndexFinger_y>CY:
                    PLACE_INDEX_FINGER="right_down"  #오른쪽 아래
                else:
                    PLACE_INDEX_FINGER="right_up"  #오른쪽 위
            if IndexFinger_x<CX:
                if IndexFinger_y>CY:
                    PLACE_INDEX_FINGER="left_down"  #왼쪽 아래
                else:
                    PLACE_INDEX_FINGER="left_up"  #왼쪽 위

        #작은 화면의 x,y를 큰 화면의 x,y에 출력
        if PLACE_INDEX_FINGER=="right_down":  #오른쪽 아래
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="right_up":  #오른쪽 위
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="left_down":  #왼쪽 아래
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="left_up":  #왼쪽 위    
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big
        else:
            print("error")

        #너비와 높이의 오버플로를 처리하고 경계에서 포화되도록 합니다.
        if AFTER_IndexFinger_x >W_BIG:
            AFTER_IndexFinger_x=W_BIG-1
        if AFTER_IndexFinger_y>H_BIG:
            AFTER_IndexFinger_y=H_BIG-1

        #매핑 후 좌표에 따라 마우스 이동
        #효과를 볼 수 있도록 화면 중앙의 작은 창을 확대하는 것을 잊지 마십시오!!
        pyautogui.moveTo(AFTER_IndexFinger_x, AFTER_IndexFinger_y) 

        #손끝의 위치를 강조
        #그러나 작은 창과 큰 창의 차이로 인해 올바르게 표시되지 않습니다. 값은 큰 창 좌표계의 값이지만 작은 창에 표시되기 때문입니다.
        #cv2.circle(frame, (int(IndexFinger_x), int(IndexFinger_y)), 15, (120, 0, 120), cv2.FILLED)

        #손끝의 위치를 강조
        #위와 같은 이유로 사용할 수 없습니다.IndexFinger_x, IndexFinger_y
        cv2.circle(frame, (int(keypoint_pos[8][0]), int(keypoint_pos[8][1])), 15, (120, 0, 120), cv2.FILLED)

    return frame

#마우스 드래그
#마우스 컨트롤처럼 매핑만 하면 됩니다.
def mouse_drag(frame, keypoint_pos):
    length, frame, lineInfo = distance(8, 12,frame, keypoint_pos) #8과 12는 검지와 중지의 끝부분입니다.。  두 손가락 끝을 연결하는 선의 중점

    #두 손가락 끝 사이의 거리가 매우 가까울 때 마우스 제어가 수행됩니다.
    if length < 50:
        cv2.circle(frame, (lineInfo[0], lineInfo[1]),15, (0, 120, 0), cv2.FILLED)

        #mapping방법---------------------------------------------------------------
        #뷰포트가 왼쪽 상단 모서리에 있을 때 손가락 끝의 좌표 범위는 다음과 같습니다. x = 0 ~ 1240, y = 0 ~ 700
        #그리고 이 컴퓨터 화면의 길이와 너비는 = 1920, 높이 = 1080
        #
        #따라서 이것을 원래 손가락 끝 좌표에서 화면의 마우스 좌표로 매핑하려면。
        # X = 1920/1240 * x
        # Y = 1080/700 * y
        #--------------------------------------------------------------------------

        #좌표를 화면 중앙으로 이동:
        # X2=X1  620  350------(DX,DY)=(+340,+190)------->960  540
        # (LX, RX) = (960-620, 960+620) = (340, 1580)
        # (LY, RY) = (540-350, 540+350) = (190, 890)
        #
        #중심을 이동한 후 이 작은 창의 테두리와 중심 좌표는 다음과 같습니다.
        # 왼쪽 위 (LX, LY) = (340, 190)   ----> (0, 0)   340-(960-340)*(340/620)=0   190-(540-190)*(190/350)=0
        # (CX, CY) = (960, 540)  ----> (960, 540)
        # 오른쪽 아래 (RX, RY) = (1580, 890)  -----> (1920, 1080)
        # 왼쪽 아래 (ldx, ldy) = (340, 890) ----> (0, 1080)
        # 오른쪽 위 (rux, ruy) = (1580, 190)  -----> (1920, 0)
        #
        #이 작은 창의 경계와 중심 좌표를 큰 창(컴퓨터 화면 크기)에 매핑하면 좌표는 다음과 같습니다.
        #작은 창 절반 너비 960-340=620，작은 창 절반 높이 540-190=350
        #큰 창 절반 너비 1920/2=960，큰 창 절반 높이 1080/2=540
        #작은 창(x,y)=(150,200)---센터로 이동---->(x,y)=(150+340, 200+190)=(490, 390)------큰 화면으로 확장----->(x,y)=(490*[], 390+)

        PLACE_INDEX_FINGER = ""  #손가락 끝이 중심을 이동한 후의 기록, 그 방향은 큰 화면의 중심을 기준으로 한 좌표(좌상단, 우하단, 좌하단, 우상단)

        #중지 손가락 끝의 좌표는 창을 왼쪽 상단에 놓았을 때의 좌표입니다.
        IndexFinger_x, IndexFinger_y = keypoint_pos[12][0], keypoint_pos[12][1] #중지 끝의 x,y 좌표

        W_LITTLE , H_LITTLE = 1240 , 700  #FIXME: 실측한건데 카메라 사이즈에 맞게 바꿔야지!!
        W_BIG , H_BIG = pyautogui.size()  #이 컴퓨터 화면 크기
        half_W_LITTLE , half_H_LITTLE = W_LITTLE/2 , H_LITTLE/2  #작은 창 절반 너비 / 작은 창 절반 높이
        half_W_BIG , half_H_BIG = W_BIG/2 , H_BIG/2  #큰 창 절반 너비 / 큰 창 절반 높이
        LX , LY = (W_BIG/2 - W_LITTLE/2) , (H_BIG/2 - H_LITTLE/2)  #왼쪽 위
        RX , RY = (W_BIG/2 + W_LITTLE/2) , (H_BIG/2 + H_LITTLE/2)  #오른쪽 아래
        CX , CY = LX + W_LITTLE / 2 , LY + H_LITTLE / 2  #중앙

        L_BIG = int(math.hypot(W_BIG, H_BIG))  #큰 창의 대각선 길이
        L_LITTLE = int(math.hypot(W_LITTLE, H_LITTLE))  #작은 창의 대각선 길이
        RATIO = L_BIG/L_LITTLE #두 창의 대각선 비율

        #작은 화면의 좌표를 큰 화면의 중앙에 인쇄
        IndexFinger_x, IndexFinger_y = IndexFinger_x + LX , IndexFinger_y + LY

        #중심을 기준으로 어느 방향을 결정합니다(왼쪽 위, 오른쪽 아래, 왼쪽 아래, 오른쪽 위).
        if IndexFinger_x==CX and IndexFinger_y==CY:
            PLACE_INDEX_FINGER="middle" #정중앙
        else:  #중앙에 없으면
            if IndexFinger_x>CX:
                if IndexFinger_y>CY:
                    PLACE_INDEX_FINGER="right_down"  #오른쪽 아래
                else:
                    PLACE_INDEX_FINGER="right_up"  #오른쪽 위
            if IndexFinger_x<CX:
                if IndexFinger_y>CY:
                    PLACE_INDEX_FINGER="left_down"  #왼쪽 아래
                else:
                    PLACE_INDEX_FINGER="left_up"  #왼쪽 위

        #작은 화면의 x,y를 큰 화면의 x,y에 출력
        if PLACE_INDEX_FINGER=="right_down":  #오른쪽 아래 
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="right_up":  #오른쪽 위
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="left_down":  #왼쪽 아래
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big

        elif PLACE_INDEX_FINGER=="left_up":  #왼쪽 위   
            dx_little = IndexFinger_x - CX
            dy_little = IndexFinger_y - CY
            dx_big = dx_little * RATIO
            dy_big = dy_little * RATIO
            AFTER_IndexFinger_x = CX + dx_big
            AFTER_IndexFinger_y = CY + dy_big
        else:
            print("error")

        #너비와 높이의 오버플로를 처리하고 경계에서 포화되도록 합니다.
        if AFTER_IndexFinger_x >W_BIG:
            AFTER_IndexFinger_x=W_BIG-1
        if AFTER_IndexFinger_y>H_BIG:
            AFTER_IndexFinger_y=H_BIG-1

        #매핑 후 좌표에 따라 파일 또는 폴더 드래그 (드래그는 "마우스 왼쪽 버튼을 누른 상태에서 이동"으로 이루어진다)
        #효과를 볼 수 있도록 화면 중앙의 작은 창을 확대하는 것을 잊지 마십시오!!
        pyautogui.dragTo(AFTER_IndexFinger_x, AFTER_IndexFinger_y, button='left', duration=1)
        #pyautogui.dragTo(AFTER_IndexFinger_x, AFTER_IndexFinger_y, button='left')

        #손끝의 위치를 강조
        #그러나 작은 창과 큰 창의 차이로 인해 올바르게 표시되지 않습니다. 값은 큰 창 좌표계의 값이지만 작은 창에 표시되기 때문입니다.
        #cv2.circle(frame, (int(IndexFinger_x), int(IndexFinger_y)), 15, (120, 0, 120), cv2.FILLED)

        #손끝의 위치를 강조
        #위와 같은 이유로 사용할 수 없습니다.IndexFinger_x, IndexFinger_y
        cv2.circle(frame, (int(keypoint_pos[12][0]), int(keypoint_pos[12][1])), 15, (120, 0, 120), cv2.FILLED)

    return frame

#제스처에 따라 다른 동작을 수행합니다.
def BEHAVIOR(frame, gesture_str, keypoint_pos, angle_list, bounding):

    global TIME_Previous_page, TIME_Next_page #너무 민감하지 않고 연속으로 너무 많이 실행하지 않고 이전 페이지로 점프하고 다음 페이지로 점프하는 것을 방지하는 데 사용됩니다.
    global delay_Next_page, delay_Previous_page #너무 민감하지 않고 연속으로 너무 많이 실행하지 않고 이전 페이지로 점프하고 다음 페이지로 점프하는 것을 방지하는 데 사용됩니다. 2초 간격을 둡니다.
    global TIME_PrintScreen #스크린샷이 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다.
    global delay_PrintScreen #스크린샷이 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다. 2초 간격을 둡니다.
    global TIME_escape_show #너무 민감하기 때문에 연속으로 너무 많이 실행하지 않도록하십시오.。
    global delay_escape_show #너무 민감하기 때문에 연속으로 너무 많이 실행하지 않도록 하십시오. 2초 간격을 둡니다.
    global TIME_show_from_begin, TIME_show_from_current #복사-붙여넣기가 너무 민감하고 연속적으로 너무 많이 실행되는 것을 방지하는 데 사용됩니다.
    global delay_show_from_begin, delay_show_from_current #너무 민감해서 복사 붙여넣기가 연속으로 너무 많이 실행되는 것을 방지하기 위해 사용합니다. 2초 간격을 둡니다.

    ##엄지 손가락, 검지손가락이 왼쪽으로 향하게 하면 PPT가 이전 페이지로 이동(각 시간 간격 지연delay_Previous_page)
    if gesture_str == "Previous_page":                               # 12_left
        LATE_TIME_Previous_page = time.time()
        if LATE_TIME_Previous_page - TIME_Previous_page > delay_Previous_page or TIME_Previous_page==0:  #각 클릭이 지연되도록 하십시오_Previous_page
            gesture_str = gesture_str
            TIME_Previous_page = time.time()

            #키보드의 왼쪽 화살표 키를 누르는 작업 --> 이전 페이지로 이동합니다.
            pyautogui.hotkey('left')

        else:
            gesture_str = gesture_str + "_"

    ##엄지 손가락, 검지손가락이 오른쪽 으로 향하게 하면 PPT가 다음 페이지로 이동(각 시간 간격 지연delay_Previous_page)
    if gesture_str == "Next_page":                                # 12_right
        LATE_TIME_Next_page = time.time()
        if LATE_TIME_Next_page - TIME_Next_page > delay_Next_page or TIME_Next_page==0:  #모든 클릭을 지연_다음_페이지 초로 설정
            gesture_str = gesture_str
            TIME_Next_page = time.time()

            #키보드의 오른쪽 화살표 키를 누르는 동작을 확인 --> 다음 페이지로 이동합니다.
            pyautogui.hotkey('right')

        else:
            gesture_str = gesture_str + "_"

    ## 손가락을 모두 펼쳤을때 스크린샷(win+PrintScreen)
    if gesture_str == "PrintScreen":                                   # 125
        LATE_TIME_PrintScreen = time.time()
        if LATE_TIME_PrintScreen - TIME_PrintScreen > delay_PrintScreen or TIME_PrintScreen==0:  #모든 클릭을 지연_다음_페이지 초로 설정
            gesture_str = gesture_str
            TIME_PrintScreen = time.time()

            #스크린샷을 찍는 작업
            pyautogui.hotkey('win', 'printscreen')

        else:
            gesture_str = gesture_str + "_"

    ## 중지 손가락, 약지 손가락, 새끼 손가락을 펼쳤을때 슬라이드 종료
    if gesture_str == "escape_show":                                 # 345
        LATE_TIME_escape_show = time.time()
        if LATE_TIME_escape_show - TIME_escape_show > delay_escape_show or TIME_escape_show==0:  #모든 클릭을 지연_다음_페이지 초로 설정
            gesture_str = gesture_str
            TIME_escape_show = time.time()

            #슬라이드쇼를 끝내기 위한 조치를 취하십시오
            pyautogui.hotkey('esc')

        else:
            gesture_str = gesture_str + "_"
            
    # 슬라이드쇼 시작
    if gesture_str == "show_from_begin":                              # 123
        LATE_TIME_show_from_begin = time.time()
        if LATE_TIME_show_from_begin - TIME_show_from_begin > delay_show_from_begin or TIME_show_from_begin==0:
            gesture_str = gesture_str
            TIME_show_from_begin = time.time()
            pyautogui.hotkey('f5')
            
        else:
                gesture_str = gesture_str + "_"
                
    # 현재 페이지 슬라이드쇼 시작            
    if gesture_str == "show_from_current":                  # 234
        LATE_TIME_show_from_current = time.time()
        if LATE_TIME_show_from_current - TIME_show_from_current > delay_show_from_current or TIME_show_from_current==0:
            gesture_str = gesture_str
            TIME_show_from_current = time.time()
            pyautogui.hotkey('shift', 'f5')
            
        else:
                gesture_str = gesture_str + "_"
     
     # 마우스 도전
    if gesture_str == "mouse":                          # 2
        LATE_TIME_mouse_move = time.time()
        pyautogui.position()
        pyautogui.size()

        while gesture_str == "mouse":
            gesture_str = gesture_str + "_"
            pyautogui.moveTo(keypoint_pos[4][0], keypoint_pos[4][1])
            
    if gesture_str == "click":                          # 0
        pyautogui.click(clicks=1)

    ##검지 손가락, 중지 손가락, 약지 손가락을 펼쳤을때
    #       검지 손가락, 중지 손가락을 붙이고, 약지 손가락을 떨어트리면 슬라이드쇼 시작
    #       且34很近(23遠)，則執行從目前的投影片開始放映  每次間隔3秒
#     if gesture_str == "234":
#         length_23, frame, lineInfo_23 = distance(8, 12,frame, keypoint_pos) #23 손끝 인덱스. lineInfo는 두 손가락 끝을 연결하는 선의 중간점입니다.
#         length_34, frame, lineInfo_34 = distance(12, 16,frame, keypoint_pos) #34 손가락 끝의 검지. lineInfo는 두 손가락 끝을 연결하는 선의 중간점입니다.

#         if length_23<30 and length_34>30:  #23이 가깝고 34가 멀 때 전면 슬라이드부터 시작합니다(23은 겹쳐야함).
#             cv2.circle(frame, (lineInfo_23[0], lineInfo_23[1]),15, (0, 136, 0), cv2.FILLED)
#             LATE_TIME_show_from_begin = time.time()
#             if LATE_TIME_show_from_begin - TIME_show_from_begin > delay_show_from_begin or TIME_show_from_begin==0:  #모든 클릭을 delay_show_from_begin 초로 설정
#                 gesture_str = gesture_str + "_show_from_begin"
#                 TIME_show_from_begin = time.time()

#                 #맨 앞 슬라이드에서 시작하는 작업 만들기
#                 pyautogui.hotkey('f5')

#             else:
#                 gesture_str = gesture_str + "_no_show_from_begin"

#         elif length_34<30 and length_23>30:  #34가 가깝고 23이 멀 때 현재 슬라이드에서 시작합니다(34는 겹쳐야함).
#             cv2.circle(frame, (lineInfo_34[0], lineInfo_34[1]),15, (80, 255, 0), cv2.FILLED)
#             LATE_TIME_show_from_current = time.time()
#             if LATE_TIME_show_from_current - TIME_show_from_current > delay_show_from_current or TIME_show_from_current==0:  #모든 클릭을 간격으로 설정delay_show_from_current秒
#                 gesture_str = gesture_str + "_show_from_current"
#                 TIME_show_from_current = time.time()

#                 #현재 슬라이드에서 표시를 시작하는 작업을 수행합니다.
#                 pyautogui.hotkey('shift', 'f5')

#             else:
#                 gesture_str = gesture_str + "_no_show_from_current"

#         else: #"34가 가깝고 23이 가깝다"(이 상황은 34로 오인되어 건너뛸 수 있음) 또는 "34가 멀고 23이 멀다"이면 아무 것도 하지 않는다.
#             return frame, gesture_str 
    
    return frame, gesture_str

@app.route('/')
def detect():
    gesture_dict_for_img_processing = {
        "Nike": None,
        "thumb up!": 11, 
        "index finger up!": None, 
        "middle finger up!": None, 
        "ring finger up!": None, 
        "little finger up!": None
    }
    mp_hands = mp.solutions.hands

    #1. static_image_mode=False ---> If set to false, the solution treats the input images as a video stream. It will try to detect hands in the first input images
    #1. static_image_mode=true ---> If set to true, hand detection runs on every input image
    #2. max_num_hands=1 ---> Maximum number of hands (1) to detect.
    hands = mp_hands.Hands(
            static_image_mode=False, #첫 번째 이미지만 감지됩니다(하지만 true로 설정하면 어떤 일이 일어날지 모릅니다. 아마도 느려질까요?), 일반적으로 false로 설정됩니다.
            max_num_hands=1,  #감지할 수 있는 최대 손 수입니다. (기본값은 2)
            min_detection_confidence=0.75,  #제스처 인식 후 신뢰도(테스트 정확도)가 있습니다. 그리고 이 매개변수는 제스처 인식의 평가 값(테스트 정확도)이 이 매개변수 값보다 커야 함을 나타내며(성공적인 인식으로 간주) 인식 결과를 사용합니다.
            min_tracking_confidence=0.75)  
        #대상 추적, 하지만 이것이 어떻게 작동하는지 모르겠습니다. (static_image_mode=true인 경우 이 매개변수를 무시할 수 있습니다.) 성공적인 추적으로 간주하려면 이 매개변수 값보다 커야 합니다.

    # 비디오 렌즈 판독기 켜기
    #그리고 index=0으로 카메라를 지정합니다(여러 카메라가 동시에 연결된 경우 다른 값을 설정해야 함)
    cap = cv2.VideoCapture(0)
        
    cv2.namedWindow('MediaPipe Hands', cv2.WINDOW_NORMAL)

    #프레임의 너비와 높이 설정 = (width, height)
    #pyautogui.size()화면 크기 반환
    width,height = pyautogui.size()  #1920, 1080
    cap.set(3,width) #너비가 프레임 너비에 지정되었음을 나타냅니다.
    cap.set(4,height)#높이가 프레임의 높이에 지정되었음을 나타냅니다.

    #"사진을 찍은 다음 사진 처리" 작업이 항상 수행됩니다.
    while True:

        # 이미지에서 손 감지
        #프레임은 사진입니다(프레임이라는 단어는 특히 필름에서 사진 중 하나를 참조하는 데 사용됩니다-----필름은 사진으로 구성되어 있지만 속도가 너무 빨라서 눈으로 연속적으로 생각합니다.)
        #사진을 찍다
        success, frame = cap.read()  #사진 찍기

        #카메라 모듈이 성공적으로 켜지지 않으면 이 반복을 건너뜁니다.
        if not success:
            print("Ignoring empty camera frame.")
            continue

        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)  #RGB로
        frame= cv2.flip(frame,1)  #좌우 뒤집기

        #mediapipe에 필요한 입력 이미지 형식은 rgb이기 때문에
        results = hands.process(frame)

        frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)#imshow()에 필요한 입력 이미지 형식을 따르려면: bgr


#        print(results.multi_hand_landmarks)  #화면에 손이 있으면 dict 유형의 많은 랜드마크가 포함된 목록을 반환합니다. (랜드마크의 x, y, z 값은 -1~1 사이) 손이 없으면 없음을 반환

#        print(results.multi_handedness)  #판단은 왼손잡이 또는 오른손잡이(출력 인덱스는 나중 판단에 사용할 수 있음)이며 식별 결과의 정확도
#        #화면에 손이 있으면 색인, 점수, 레이블이 포함된 분류 개체를 반환합니다.  
#        #       왼손잡이이면 index=0이고 레이블이 "left"이고 점수 출력은 "left-handed (left-handed and right-handed --classification)"의 신뢰도입니다.
#        #       오른손인 경우 index=1이고 레이블이 "right"이고 점수는 "confidency of the right hand(left hand right-classification)"을 출력합니다.
#        #화면에 손이 없으면 아무 것도 반환하지 않습니다.

        if results.multi_hand_landmarks:  #화면에 손이 있으면 많은 랜드마크 개체가 포함된 목록이 반환됩니다(랜드마크 개체는 x, y 및 z 값이 모두 -1에서 1 사이임). 손이 없으면 아무 것도 반환하지 않습니다.
            for hand_landmarks in results.multi_hand_landmarks:  #각 hand_landmarks는 x, y, z 값을 가진 랜드마크 객체입니다.

                ##21개의 노드를 표시합니다.
                # -------------------------------------------------------------------------------------------------------------------------
                # -------------------------------------------------------------------------------------------------------------------------
                #컬렉션을 소개하고 별칭을 지정합니다.
                mp_drawing = mp.solutions.drawing_utils
                mp_drawing_styles = mp.solutions.drawing_styles\
                
                #이 줄은 해당 컬렉션을 사용하여 해당 21개 노드를 표시합니다.
                #제스처의 해당 라인과 노드를 추가하십시오.
                mp_drawing.draw_landmarks(
                    frame, 
                    hand_landmarks, 
                    mp_hands.HAND_CONNECTIONS,
                    mp_drawing_styles.get_default_hand_landmarks_style(),
                    mp_drawing_styles.get_default_hand_connections_style()
                )
                # -------------------------------------------------------------------------------------------------------------------------
                # -------------------------------------------------------------------------------------------------------------------------


                keypoint_pos = []  #keypoint_pos 21개 노드의 x, y 값이 있을 것입니다(해당 x, y 값은 튜플에 저장됨)

                #hand_landmarks.landmark[i]를 사용하여 손바닥의 21개 노드([0]...[20])를 호출합니다. 이 21개 노드는 모두 랜드마크 객체에 있습니다.
                #landmark object 그 안에 x, y, z 값이 있고
                #x는 hand_landmarks.landmark[i].x로 호출됩니다.
                # y는 hand_landmarks.landmark[i].y로 호출됩니다.
                # x,y는 노드의 x,y 값입니다(그러나 그 값은 -1과 1 사이입니다)
                for i in range(21):
                    x = hand_landmarks.landmark[i].x * frame.shape[1]  #frame.shape[1]은 img의 G 채널입니다.
                    y = hand_landmarks.landmark[i].y * frame.shape[0]  #frame.shape[0]은 img의 B 채널입니다.

                    #keypoint_pos는 21개 노드의 x, y 값을 저장합니다(해당 x, y 값은 튜플에 저장됨)
                    #따라서 keypoint_pos의 길이는 21이고 유형은 목록입니다.
                    keypoint_pos.append((x,y))  #많은 (x, y) 유형의 튜플을 목록에 넣습니다.

                bounding = []

                #경계를 찾아라
                if keypoint_pos: 
                    #print("{}       {}".format(keypoint_pos[0],keypoint_pos[4]))
                    xmin, xmax = min(keypoint_pos[:][0]), max(keypoint_pos[:][0])
                    ymin, ymax = min(keypoint_pos[:][1]), max(keypoint_pos[:][1])
                    bounding = [xmin, ymin, xmax, ymax]


                # 이미지에 손이 있다면 이 21개 노드의 x, y 값을 저장하는 keypoint_pos 목록이 이때 사진에 대해 설정된다.
                # 이미지에 손이 없으면 keypoint_pos는 빈 목록입니다.

                #keypoint_pos가 비어 있지 않으면 keypoint_pos는 다음과 같습니다.
                #   [2]....[4]는 엄지손가락 / [5]....[8]은 검지손가락 / [9]....[12]는 중지손가락 / [13]...[16] 는 약지손가락 / [17]...[20] 은 새끼손가락

                # 이 경우 프로그램은 이미지에 손이 있을 때만 실행됩니다.
                if keypoint_pos:  

                    #다음으로 21개 노드의 위치에 따라 각도를 계산하여 제스처를 판단하게 됩니다.
                    # -------------------------------------------------------------------------------------------------------------------------
                    # -------------------------------------------------------------------------------------------------------------------------

                    # angle_list는 5개의 손가락의 개별 각도를 포함하여 길이가 5이고 요소 값이 0에서 180 사이인 목록입니다.
                    #검지를 비교적 크게 구부렸을 때(각도가 상대적으로 큰 경우) angle_list[1]의 값은 40보다 클 것입니다.
                    #검지가 완전히 펴졌을 때 angle_list[1]의 값은 40보다 작습니다.
                    angle_list = hand_angle(keypoint_pos)

                    # 제스처가 각도에 따라 검지 손가락을 뻗는 것뿐인지 확인하십시오.
                    # 검지손가락만 펼친 경우 "검지손가락 위로!"라는 문자열을 반환합니다.  
                    # 제스처가 집게 손가락을 펼치지 않으면 없음을 반환합니다.
                    gesture_str = hand_gesture(angle_list, keypoint_pos)
                    # -------------------------------------------------------------------------------------------------------------------------
                    # -------------------------------------------------------------------------------------------------------------------------

                    #제스처_str(즉, 인식된 제스처 이름)에 따라 제스처의 해당 처리를 수행합니다.
                    frame, gesture_str = BEHAVIOR(frame, gesture_str, keypoint_pos, angle_list, bounding)

                    cv2.putText(frame, gesture_str, (10, 60), cv2.FONT_HERSHEY_PLAIN, 3, (255, 0, 0), 3)

        #디스플레이 이미지
        cv2.imshow('MediaPipe Hands', frame) #imshow()에 의해 입력된 이미지는 bgr 형식이어야 하므로 이 때 프레임은 bgr 형식입니다.

        if cv2.waitKey(1) & 0xFF == 27:
            break

    cap.release()  #실행이 끝나면 카메라 리소스가 해제됩니다.

if __name__ == '__main__':
    app.run(debug=True, port=5000, host='127.0.0.1')