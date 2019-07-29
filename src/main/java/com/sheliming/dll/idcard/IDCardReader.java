package com.sheliming.dll.idcard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class IDCardReader {
    public static Map<String,String> getInfos(int type){
        return getInfos(null,type);
    }

    /**
     * @Author henry
     * @Description 读取身份证
     * @Date 2019-03-22 9:52
     * @Param saveDir 图片存储路径
     * @Param type 类型 1：身份证 2：ic卡
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> getInfos(String saveDir, int type){
        Map<String, String> paramMap = new HashMap<String, String>();
        int ret;
        int iPort=1001;		//USB PORT
        ret= IDCardDll.INSTANCE.InitComm(iPort);
        if(ret!=1){
            paramMap.put("msg","没有找到读卡器!");
        }else{
            System.out.println("读卡器连接成功!");
            if (type == 1){
                paramMap = getCardInfos(ret,paramMap,saveDir);
            }else{
                paramMap = getIcCardNo(paramMap);
            }
        }
        ret= IDCardDll.INSTANCE.CloseComm();
        if(ret!=1){
            System.out.println("读卡器关闭连接错误!");
        }else{
            System.out.println("读卡器关闭连接成功!");
        }
        return paramMap;
    }

    /**
     * @Author henry
     * @Description 获取IC卡 卡号
     * @Date 2019-03-22 9:52
     * @Param
     * @return
     */
    private static Map<String,String> getIcCardNo(Map<String,String> paramMap){
        try {
            byte[] sn = new byte[1024];
            int res = IDCardDll.INSTANCE.Routon_IC_HL_ReadCardSN(sn);
            if (res > 0){
                IDCardDll.INSTANCE.HID_BeepLED(true,true,200); // 开启蜂鸣
                paramMap.put("sn", new String(sn, "GBK").trim());
            }else{
                paramMap.put("msg","读卡失败！请将卡重新放读卡器上！");
            }
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        return paramMap;
    }

    /**
     * @Author henry
     * @Description 读取身份证信息
     * @Date 2019-03-22 10:06
     * @Param
     * @return
     */
    private static Map<String,String> getCardInfos(int ret, Map<String,String> paramMap, String saveDir){
        ret= IDCardDll.INSTANCE.Authenticate();
        if(ret!=1){
            paramMap.put("msg","读卡失败！请将身份证重新放读卡器上！");
        }else{
            byte[] Name = new byte[31];
            byte[] Gender = new byte[3];
            byte[] Folk = new byte[10];
            byte[] BirthDay = new byte[9];
            byte[] Code = new byte[19];
            byte[] Address = new byte[71];
            byte[] Agency = new byte[31];
            byte[] ExpireStart = new byte[9];
            byte[] ExpireEnd = new byte[9];
            byte[] directory; //设置照片存放目录
            if (saveDir == null || "".equals(saveDir)){
                directory = new byte[200];
            }else{
                directory = saveDir.getBytes();
            }

            ret= IDCardDll.INSTANCE.ReadBaseInfosPhoto(Name, Gender, Folk, BirthDay, Code, Address, Agency, ExpireStart, ExpireEnd, directory);
            if(ret>0){
                System.out.println("信息读取成功!");
                try {
                    System.out.println("-----------------------------------------------");
                    paramMap.put("Name", new String(Name, "GBK").trim());
                    paramMap.put("Gender", new String(Gender, "GBK").trim());
                    paramMap.put("Folk", new String(Folk, "GBK").trim());
                    paramMap.put("BirthDay", new String(BirthDay, "GBK").trim());
                    paramMap.put("Code", new String(Code, "GBK").trim());
                    paramMap.put("Address", new String(Address, "GBK").trim());
                    paramMap.put("Agency", new String(Agency, "GBK").trim());
                    paramMap.put("ExpireStart", new String(ExpireStart, "GBK").trim());
                    paramMap.put("ExpireEnd", new String(ExpireEnd, "GBK").trim());
                    paramMap.put("directory", new String(directory, "GBK").trim());
                    System.out.println("-----------------------------------------------");
                } catch (UnsupportedEncodingException e) {
                    paramMap.put("msg","卡信息异常!");
                    e.printStackTrace();
                }
            }
        }
        return paramMap;
    }

    public static void main(String[] args) {
        System.out.println(getInfos(1).toString());
    }
}
