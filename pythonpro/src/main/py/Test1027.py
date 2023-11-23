import sys
import os
import User as user
import math
import random
import _thread
import time

if __name__ == '__main__':
 i = 3
ab = "world!"
BC = "WORLD!"
c= 1000 / 21
print(ab,c,i)
v,b,n = (23,"aaaa", 234)
print(v,b,n)

ab = "HELLO"
BC = "adf"
print(ab)
print(BC)
print(type(v))


#name = input("Enter your name:")
#print("your name is:" + name)

print("你好 世界")

print(type(sys.argv))
print(sys.argv)

m = 26
n = 16

if (m > n):
    print("m 大于 n")
else:
    print("m 小于 n")



def add(a,b):
    return a+b

mm = add(12, 32)
print(mm)


#def student(name,score):
#    print(f"{name}的分数为{score}")
#n = input('请输入学生名字：')
#n1 = input("请输入学生分数:")

#student(score=n1,name=n)

name = user.getName("尚", "松")
print("my name is :" + name)

str = "shangsong"
for a in str:
    print("当前字母: %s" % a)

fruits = ['orange','apple','banana']
for index in range(len(fruits)):
    print('当前水果： %s' % fruits[index])


for num in range(20,30):
    if num % 2 == 0:
        print(num)
    else:
        print((num + 1)/2)

num = 1
while num < 10:
    print(num)
    num += 2

nums = [15, 25, 33, 22, 26]
even = []
odd = []
while len(nums) > 0 :
    number = nums.pop()
    if number % 2 == 0:
        even.append(number)
    else:
        odd.append(number)
print(even)
print(odd)

var = 1
#while var == 1:
#    ad = int(input("Enter a int number :"))
#    if ad == 0:
#        break
#    else:
#        print("You entered: ", ad)

#flag = 1
#while(flag):
#    print('Given flag is really true!')

print(abs(-156))

list1 = [12,"15"]
print(list1)
del list1[1]
print(list1)
list2 = [49, 16, 89]
list3 = list2 + list1
print(list3)

list4 = ['hello'] * 4
print(list4)
print(len(list4))

if 16 in list2:
    print("list2 中包含：16")

for i in list2:
    if i % 2 == 0:
        print(i)
    else:
        print("lalalalal")

list1.extend(list2)

print(list1)

list1.pop()

print(list1.index(16))

list1.insert(2, 999)

list1.reverse()

list1.sort()

print(list1)

for letter in 'Python':
    if letter == 'h':
        pass
        print('这是 pass 块')
    print('当前字母 :', letter)



a = "Hello"
b = "Python"

print("a + b 输出结果：", a + b)
print("a * 2 输出结果：", a * 2)
print("a[1] 输出结果：", a[1])
print("a[1:4] 输出结果：", a[1:4])

if( "H" in a) :
    print("H 在变量 a 中")
else :
    print("H 不在变量 a 中")

if( "M" not in a) :
    print("M 不在变量 a 中")
else :
    print("M 在变量 a 中")

print(r'\n')
print(R'\n')

a = 'sshha'
b = a.capitalize()
print(b)


# 为线程定义一个函数
def print_time( threadName, delay):
    count = 0
    while count < 5:
        time.sleep(delay)
        count += 1
        print("%s: %s" % ( threadName, time.ctime(time.time()) ))

# 创建两个线程
try:
    _thread.start_new_thread( print_time, ("Thread-1", 2, ) )
    _thread.start_new_thread( print_time, ("Thread-2", 4, ) )
except:
    print("Error: unable to start thread")


