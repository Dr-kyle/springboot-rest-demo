package com.kyle.demo;


import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    public static void main(String[] args) {
        // int[] nums = {-4,-1,0,3,10};
        // System.out.println(sortedSquares(nums));
        int[] nums = {1,2,3,4,5,6,7};
        int k = 3;
        rotate(nums, k);
    }

    // 双指针，给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
    public static int[] sortedSquares(int[] nums) {
        int right = nums.length - 1;
        int left = 0;
        int leftNum, rightNum;
        int[] result = new int[nums.length];
        int i = nums.length - 1;
        while (left <= right) {
            leftNum = nums[left] * nums[left];
            rightNum = nums[right] * nums[right];
            if (leftNum > rightNum) {
                result[i] = leftNum;
                left++;
                i--;
            } else if (leftNum == rightNum) {
                result[i] = leftNum;
                // 注意判断当 leftNum 是最后一个数时
                if (i > 0) {
                    result[i - 1] = leftNum;
                    i = i - 2;
                }
                left++;
                right--;
            } else {
                result[i] = rightNum;
                right--;
                i--;
            }
        }
        return result;
    }


    // 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
    // 你可以使用空间复杂度为 O(1) 的 原地 算法解决这个问题吗？
    public static void rotate(int[] nums, int k) {
        int move = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, move - 1);
        reverse(nums, move, nums.length - 1);
    }
    public static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}
