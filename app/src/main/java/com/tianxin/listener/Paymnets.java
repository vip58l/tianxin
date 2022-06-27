package com.tianxin.listener;

import android.view.View;

import com.tianxin.Module.api.moneylist;

import java.util.List;

/**
 * 自定义相关接口回调数据
 */
public interface Paymnets<T> {
    default void isNetworkAvailable() {
    }

    default void success(String date) {

    }

    default void fall(int code) {
    }

    default void search(String content) {

    }

    default void cancellation() {
    }

    default void onClick() {
    }

    default void dismiss() {
    }

    default void payens() {
    }

    default void pay() {
    }

    default void activity() {
    }

    default void activity(String str) {
    }

    default void onClick(View view) {
    }

    default void onClick(Object object) {
    }

    default void Money(double money) {
    }

    default void status(int position) {
    }

    default void returnlt(Object obj) {
    }

    default void msg(String Messsend) {
    }

    default void onError() {
    }

    default void onSuccess(Object object) {

    }

    default void onSuccess(Object object, int Type) {

    }

    default void onSuccess(String msg) {

    }

    default void onSuccess() {
    }

    default void onFail() {
    }

    default void onFail(String msg) {
    }

    default void onRefresh() {
    }

    default void onLoadMore() {
    }

    default void onItemClick(int position) {
    }

    default void onItemClick(View view, int position) {
    }

    default void payonItemClick(String date, int TYPE) {
    }

    default void payonItemClick(moneylist moneylist, int TYPE) {
    }

    default void returnltonItemClick(Object object, int TYPE) {
    }

    default void ToKen(String msg) {
    }

    default void loginwx() {
    }

    default void loginqq() {
    }

    default void onSuccessCAll() {
    }

    default void onSuccessCAll(Object object) {
    }

    default void onSucces(T object) {
    }

    default void onSucces(List<T> object) {
    }

    default void onSucces(Object obj1, Object obj2) {
    }
}