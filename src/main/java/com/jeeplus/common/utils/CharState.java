package com.jeeplus.common.utils;
/***
 * 
 * @ClassName: CharState
 * @Description:
 * 		TODO 判断一个字符串是否是json格式字符串的辅助类
 * @Date:     2015-1-27 下午09:10:36
 * @author    ygq
 * @version   mis_alpha
 * @since     JDK 1.6
 */
public class CharState{
    boolean jsonStart = false;//以 "{"开始了...
    boolean setDicValue = false;// 可以设置字典值了。
    boolean escapeChar = false;//以"\"转义符号开始了
   /// <summary>
   /// 数组开始【仅第一开头才算】，值嵌套的以【childrenStart】来标识。
   /// </summary>
    boolean arrayStart = false;//以"[" 符号开始了
    boolean childrenStart = false;//子级嵌套开始了。
   /// <summary>
   /// 【0 初始状态，或 遇到“,”逗号】；【1 遇到“：”冒号】
   /// </summary>
    int state = 0;

   /// <summary>
   /// 【-1 取值结束】【0 未开始】【1 无引号开始】【2 单引号开始】【3 双引号开始】
   /// </summary>
    int keyStart = 0;
   /// <summary>
   /// 【-1 取值结束】【0 未开始】【1 无引号开始】【2 单引号开始】【3 双引号开始】
   /// </summary>
    int valueStart = 0;
    boolean isError = false;//是否语法错误。

    void CheckIsError(char c)//只当成一级处理（因为GetLength会递归到每一个子项处理）
   {
       if (keyStart > 1 || valueStart > 1)
       {
           return;
       }
       //示例 ["aa",{"bbbb":123,"fff","ddd"}] 
       switch (c)
       {
           case '{'://[{ "[{A}]":[{"[{B}]":3,"m":"C"}]}]
               isError = jsonStart && state == 0;//重复开始错误 同时不是值处理。
               break;
           case '}':
               isError = !jsonStart || (keyStart != 0 && state == 0);//重复结束错误 或者 提前结束{"aa"}。正常的有{}
               break;
           case '[':
               isError = arrayStart && state == 0;//重复开始错误
               break;
           case ']':
               isError = !arrayStart || jsonStart;//重复开始错误 或者 Json 未结束
               break;
           case '"':
           case '\'':
               isError = !(jsonStart || arrayStart); //json 或数组开始。
               if (!isError)
               {
                   //重复开始 [""",{"" "}]
                   isError = (state == 0 && keyStart == -1) || (state == 1 && valueStart == -1);
               }
               if (!isError && arrayStart && !jsonStart && c == '\'')//['aa',{}]
               {
                   isError = true;
               }
               break;
           case ':':
               isError = !jsonStart || state == 1;//重复出现。
               break;
           case ',':
               isError = !(jsonStart || arrayStart); //json 或数组开始。
               if (!isError)
               {
                   if (jsonStart)
                   {
                       isError = state == 0 || (state == 1 && valueStart > 1);//重复出现。
                   }
                   else if (arrayStart)//["aa,] [,]  [{},{}]
                   {
                       isError = keyStart == 0 && !setDicValue;
                   }
               }
               break;
           case ' ':
           case '\r':
           case '\n'://[ "a",\r\n{} ]
           case '\0':
           case '\t':
               break;
           default: //值开头。。
               isError = (!jsonStart && !arrayStart) || (state == 0 && keyStart == -1) || (valueStart == -1 && state == 1);//
               break;
       }
   }
/// <summary>
/// 设置字符状态(返回true则为关键词，返回false则当为普通字符处理）
/// </summary>
public static boolean SetCharState(char c,  CharState cs)
{
   cs.CheckIsError(c);
   switch (c)
   {
       case '{'://[{ "[{A}]":[{"[{B}]":3,"m":"C"}]}]
           if (cs.keyStart <= 0 && cs.valueStart <= 0)
           {
               cs.keyStart = 0;
               cs.valueStart = 0;
               if (cs.jsonStart && cs.state == 1)
               {
                   cs.childrenStart = true;
               }
               else
               {
                   cs.state = 0;
               }
               cs.jsonStart = true;//开始。
               return true;
           }
           break;
       case '}':
           if (cs.keyStart <= 0 && cs.valueStart < 2 && cs.jsonStart)
           {
               cs.jsonStart = false;//正常结束。
               cs.state = 0;
               cs.keyStart = 0;
               cs.valueStart = 0;
               cs.setDicValue = true;
               return true;
           }
           // cs.isError = !cs.jsonStart && cs.state == 0;
           break;
       case '[':
           if (!cs.jsonStart)
           {
               cs.arrayStart = true;
               return true;
           }
           else if (cs.jsonStart && cs.state == 1)
           {
               cs.childrenStart = true;
               return true;
           }
           break;
       case ']':
           if (cs.arrayStart && !cs.jsonStart && cs.keyStart <= 2 && cs.valueStart <= 0)//[{},333]//这样结束。
           {
               cs.keyStart = 0;
               cs.valueStart = 0;
               cs.arrayStart = false;
               return true;
           }
           break;
       case '"':
       case '\'':
           if (cs.jsonStart || cs.arrayStart)
           {
               if (cs.state == 0)//key阶段,有可能是数组["aa",{}]
               {
                   if (cs.keyStart <= 0)
                   {
                       cs.keyStart = (c == '"' ? 3 : 2);
                       return true;
                   }
                   else if ((cs.keyStart == 2 && c == '\'') || (cs.keyStart == 3 && c == '"'))
                   {
                       if (!cs.escapeChar)
                       {
                           cs.keyStart = -1;
                           return true;
                       }
                       else
                       {
                           cs.escapeChar = false;
                       }
                   }
               }
               else if (cs.state == 1 && cs.jsonStart)//值阶段必须是Json开始了。
               {
                   if (cs.valueStart <= 0)
                   {
                       cs.valueStart = (c == '"' ? 3 : 2);
                       return true;
                   }
                   else if ((cs.valueStart == 2 && c == '\'') || (cs.valueStart == 3 && c == '"'))
                   {
                       if (!cs.escapeChar)
                       {
                           cs.valueStart = -1;
                           return true;
                       }
                       else
                       {
                           cs.escapeChar = false;
                       }
                   }

               }
           }
           break;
       case ':':
           if (cs.jsonStart && cs.keyStart < 2 && cs.valueStart < 2 && cs.state == 0)
           {
               if (cs.keyStart == 1)
               {
                   cs.keyStart = -1;
               }
               cs.state = 1;
               return true;
           }
           // cs.isError = !cs.jsonStart || (cs.keyStart < 2 && cs.valueStart < 2 && cs.state == 1);
           break;
       case ',':
           if (cs.jsonStart)
           {
               if (cs.keyStart < 2 && cs.valueStart < 2 && cs.state == 1)
               {
                   cs.state = 0;
                   cs.keyStart = 0;
                   cs.valueStart = 0;
                   //if (cs.valueStart == 1)
                   //{
                   //    cs.valueStart = 0;
                   //}
                   cs.setDicValue = true;
                   return true;
               }
           }
           else if (cs.arrayStart && cs.keyStart <= 2)
           {
               cs.keyStart = 0;
               //if (cs.keyStart == 1)
               //{
               //    cs.keyStart = -1;
               //}
               return true;
           }
           break;
       case ' ':
       case '\r':
       case '\n'://[ "a",\r\n{} ]
       case '\0':
       case '\t':
           if (cs.keyStart <= 0 && cs.valueStart <= 0) //cs.jsonStart && 
           {
               return true;//跳过空格。
           }
           break;
       default: //值开头。。
           if (c == '\\') //转义符号
           {
               if (cs.escapeChar)
               {
                   cs.escapeChar = false;
               }
               else
               {
                   cs.escapeChar = true;
                   return true;
               }
           }
           else
           {
               cs.escapeChar = false;
           }
           if (cs.jsonStart || cs.arrayStart) // Json 或数组开始了。
           {
               if (cs.keyStart <= 0 && cs.state == 0)
               {
                   cs.keyStart = 1;//无引号的
               }
               else if (cs.valueStart <= 0 && cs.state == 1 && cs.jsonStart)//只有Json开始才有值。
               {
                   cs.valueStart = 1;//无引号的
               }
           }
           break;
   }
   return false;
}
}