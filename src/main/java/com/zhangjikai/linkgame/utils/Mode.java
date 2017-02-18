package com.zhangjikai.linkgame.utils;

/**
 * 创建棋盘时提供的棋盘模式
 * 作为Checker中newGame方法最后一个参数被使用
 * 具体内容请参考定义
 * Created on  5/9/2013
 * @author andong
 *
 */
public enum Mode {
	classic,	//经典棋盘，图形充满棋盘，棋盘形态不会改变
	blank,		//允许生成的棋盘中有空白，棋盘形态不会改变
	up,			//棋盘中的图形自动上移，图形充满棋盘
	down,		//棋盘中的图形自动下移，图形充满棋盘
	left,		//棋盘中的图形自动左移，图形充满棋盘
	right,		//棋盘中的图形自动右移，图形充满棋盘
	center;		//棋盘中的图形自动向中心聚集，图形充满棋盘
}
