package com.sheliming.dll.idcard;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface IDCardDll extends StdCallLibrary {
    //		String path = ReadCardUtils.class.getResource("/").getPath().substring(1).replace("/", "\\") + "Sdtapi";
    String path2 = "sdtapi";
    ReadCardUtils.DLL INSTANCE = (ReadCardUtils.DLL)Native.loadLibrary(path2, ReadCardUtils.DLL.class);

    //卡认证接口
    int InitComm(int iPort);
    int CloseComm();
    int Authenticate();
    //读卡信息接口
    int ReadBaseMsg(char[] pMsg, int len);
    int ReadBaseMsgPhoto(char[] pMsg, int len, char[] directory);
    int ReadBaseInfos(char[] Name, char[] Gender, char[] Folk,
                      char[] BirthDay, char[] Code, char[] Address, char[] Agency, char[] ExpireStart, char[] ExpireEnd);
    int ReadBaseInfosPhoto(byte[] Name, byte[] Gender, byte[] Folk,
                           byte[] BirthDay, byte[] Code, byte[] Address,
                           byte[] Agency, byte[] ExpireStart,
                           byte[] ExpireEnd, byte[] directory);//此处使用了这个接口读取信息!注意只能用byte[],不能是char[];否则会乱码!转都转不了!
    int ReadBaseMsgW(char[] pMsg, int len);
    int ReadBaseMsgWPhoto(char[] pMsg, int[] len, char[] directory);
    //读追加地址信息
    int ReadNewAppMsg(char[] pMsg, int num);
    int ReadNewAppInfos(char[] addr1,
                        char[] addr2, char[] addr3,
                        char[] addr4, int num);
    int ReadNewAppMsgW(char[] pMsg, int[] num);
    //读卡体管理号
    int ReadIINSNDN(char[] pMsg);
    //读模块序列号
    int GetSAMIDToStr(char[] pcSAMID);

    int Routon_IC_FindCard(); // 查询ic卡类型

    int Routon_IC_HL_ReadCardSN(byte[] SN); // 读取IC卡 卡号

    int HID_BeepLED(boolean BeepON, boolean LEDON, int duration); // 开启蜂鸣声
}