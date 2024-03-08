package com.ss.song.test;

import org.bouncycastle.crypto.signers.ISOTrailers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FIndMidNum {

    public static void main(String[] args) {
        int[] num1 = {1, 4, 5, 7, 8};
        int[] num2 = {3, 6, 9};
        System.out.println(findMedianSortedArrays(num1, num2));
    }
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        List<Integer> list = new ArrayList<>();
        for (int a : nums1) {
            list.add(a);
        }
        for (int b : nums2) {
            list.add(b);
        }
        List<Integer> collect = list.stream().sorted().collect(Collectors.toList());
        System.out.println(collect);
        int index = collect.size() / 2;
        if (collect.size() % 2 == 0) {
            return  (collect.get(index -1) + collect.get(index)) / 2.0;
        } else {
            return (double)collect.get(index);
        }
    }
}
