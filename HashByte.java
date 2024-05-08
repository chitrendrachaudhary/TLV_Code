package common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HashByte {
    public static final byte BOTH_OPERATOR = 66;
    public static final byte AND_OPERATOR = 65;
    public static final byte CONTINUE = 67;
    public static final byte RETURN = 82;
    public static final byte SUCCESS = 83;
    public static final byte OR_OPERATOR = 79;
    public static byte prefix[] = "".getBytes();

    public static int byteAppend(int place, byte b[], TLVArray[] msg_Hash, int index_count, short index[]) {
        arraycopy(b, 0, msg_Hash[place].data, 0, b.length);
        msg_Hash[place].len = b.length;
        index[index_count] = (short) place;
        index_count++;
        return index_count;
    }

    public static int intAppend(int place, byte b[], TLVArray[] msg_Hash, int index_count, int index[], int location, int length) {
        arraycopy(b, 0, msg_Hash[location].data, 0, length);
        msg_Hash[location].len = length;
        index[index_count] = location;
        msg_Hash[place].tag = place;
        index_count++;
        return index_count;
    }

    public static int byteAppendSort(int place, byte b[], TLVArraySort[] msg_Hash, int index_count, short index[]) {
        arraycopy(b, 0, msg_Hash[place].data, 0, b.length);
        msg_Hash[place].len = b.length;
        index[index_count] = (short) place;
        index_count++;
        return index_count;
    }

    public static void arraycopy(byte src[], int src_st, byte des[], int des_st, int len) {
        for (int l = 0; l < len; l++) {
            //System.out.println("des_st+l" +des_st+l +"    "+src_st+l);
            des[des_st + l] = src[src_st + l];
        }

    }

    public static int getArrayFromByte(byte ar[], int length, TLVArray[] msg_Hash, short[] index, int index_count) {
        short tag, len;
        byte[] temp = new byte[4];
        int temp_i = 0;
        for (int i = 0; i < length; ) {
            temp_i = 0;
//			System.out.println("i "+i);
            arraycopy(ar, i, temp, 0, 4);
            int bit = (temp[0] >> 7) & 0x01;
            if (bit == 1) {
                tag = Byte_to_sort(0, temp);
                i++;
                temp_i++;
            } else {
                tag = ar[i];
            }
            bit = (temp[temp_i + 1] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(temp_i + 1, temp);
                i++;

            } else {
                len = ar[i + 1];
            }
            //		System.out.println(tag+" len "+len + " temp_i "+ temp_i);
            arraycopy(ar, i + 2, msg_Hash[tag].data, 0, len);
            msg_Hash[tag].len = len;
            index[index_count] = tag;
            i = i + 2 + len;
            index_count++;
        }
        return index_count;
    }

    public static int getArrayFromTagByte_KPI(byte ar[], int length, byte[] print, String ty, byte[] tid) {
        HashByte.arraycopy(prefix, 0, print, 0, prefix.length);

        byte[] ar2 = new byte[2];
        ar2[0] = ar[0];
        ar2[1] = ar[1];
        length = (int) Byte_to_sort(0, ar2);
        int len2 = prefix.length;
        byte[] temp3 = ty.getBytes();
        byte[] temp2 = "] ".getBytes();
        byte[] tt = " ".getBytes();
        arraycopy(temp3, 0, print, len2, temp3.length);
        len2 = len2 + temp3.length;
        arraycopy(tid, 0, print, len2, tid.length);
        len2 = len2 + tid.length;
        for (int i = 2; i < length; ) {
            short tag;
            short len;
            ar2[0] = ar[i];
            ar2[1] = ar[i + 1];
            int bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                tag = Byte_to_sort(0, ar2);
                i++;

            } else {
                tag = ar2[0];
            }
            ar2[0] = ar[i + 1];
            ar2[1] = ar[i + 2];
            bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(0, ar2);
                i++;

            } else {
                len = ar2[0];
            }
            byte temp[] = (" [" + tag + ":").getBytes();
            arraycopy(temp, 0, print, len2, temp.length);
            len2 = len2 + temp.length;
            int type = ar[i + 2];
            int val = ar[i + 3];

            arraycopy(ar, i + 2, print, len2, 1);
            len2 = len2 + 1;
            arraycopy(tt, 0, print, len2, tt.length);
            len2 = len2 + tt.length;
            arraycopy(ar, i + 3, print, len2, 1);
            len2 = len2 + 1;
            arraycopy(tt, 0, print, len2, tt.length);
            len2 = len2 + tt.length;
            if (ar[i + 4] < 20) {
                String ttt = "  ";
                for (int j = 0; j < (len - 2); j++) {
                    ttt = ttt + ar[i + 4 + j] + "-";
                }
                byte[] ttt2 = (ttt.substring(0, ttt.length() - 1)).getBytes();
                arraycopy(ttt2, 0, print, len2, ttt2.length);
                len2 = len2 + ttt2.length;
            } else {
                arraycopy(ar, i + 4, print, len2, len - 2);
                len2 = len2 + len - 2;
            }
            arraycopy(temp2, 0, print, len2, temp2.length);
            len2 = len2 + temp2.length;
            i = i + 2 + len;
        }
        return len2;
    }

    public static int getArrayFromTagByte(byte ar[], int length, byte[] print) {
        HashByte.arraycopy(prefix, 0, print, 0, prefix.length);
        short tag, len;
        int len2 = prefix.length;
        byte[] ar2 = new byte[2];
        byte[] temp3 = "TLV ".getBytes();
        byte[] temp2 = "] ".getBytes();
        arraycopy(temp3, 0, print, len2, temp3.length);
        len2 = len2 + temp3.length;
        for (int i = 0; i < length; ) {
            ar2[0] = ar[i];
            ar2[1] = ar[i + 1];
            int bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                tag = Byte_to_sort(0, ar2);
                i++;

            } else {
                tag = ar2[0];
            }
            ar2[0] = ar[i + 1];
            ar2[1] = ar[i + 2];
            bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(0, ar2);
                i++;

            } else {
                len = ar2[0];
            }
            byte temp[] = (" [" + tag + ":").getBytes();
            arraycopy(temp, 0, print, len2, temp.length);
            len2 = len2 + temp.length;
            arraycopy(ar, i + 2, print, len2, len);
            len2 = len2 + len;
            arraycopy(temp2, 0, print, len2, temp2.length);
            len2 = len2 + temp2.length;
            i = i + 2 + len;
        }
        return len2;
    }

    public static int getArrayFromTagByteFull(byte ar[], int length, byte[] print) {
        HashByte.arraycopy(prefix, 0, print, 0, prefix.length);
        short tag, len;
        int len2 = prefix.length;
        byte[] ar2 = new byte[2];
        byte[] temp3 = "TLV ".getBytes();
        byte[] temp2 = "] ".getBytes();
        arraycopy(temp3, 0, print, len2, temp3.length);
        len2 = len2 + temp3.length;
        for (int i = 0; i < length; ) {
            ar2[0] = ar[i];
            ar2[1] = ar[i + 1];
            len = 0;
            int bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                tag = Byte_to_sort(0, ar2);
                i++;

            } else {
                tag = ar2[0];
            }
            ar2[0] = ar[i + 1];
            ar2[1] = ar[i + 2];
            bit = (ar2[0] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(0, ar2);
                i++;

            } else {
                len = ar2[0];
            }
            byte temp[] = (" [" + tag + ":").getBytes();
            arraycopy(temp, 0, print, len2, temp.length);
            len2 = len2 + temp.length;
            String ttt = " ";
            for (int j = 0; j < len; j++) {
                if (i + 2 + j >= length)
                    break;
                ttt = ttt + ar[i + 2 + j] + "-";
            }
            byte[] ttt2 = (ttt.substring(0, ttt.length() - 1)).getBytes();
            arraycopy(ttt2, 0, print, len2, ttt2.length);
            len2 = len2 + ttt2.length;

            //      arraycopy(ar,i+2,print,len2,len);
            //    len2 = len2+len;

            arraycopy(temp2, 0, print, len2, temp2.length);
            len2 = len2 + temp2.length;
            i = i + 2 + len;
        }
        return len2;
    }

    public static int getByteArray(TLVArray[] msg_Hash, short[] index, byte[] result) throws Exception {
        int ind = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == 0)
                return ind;
            if (msg_Hash[index[i]].len == -1)
                continue;
            if (index[i] > 127) {
                sort_to_Byte(index[i], result, ind);
                ind += 2;
            } else {
                result[ind] = (byte) index[i];
                ind += 1;
            }
            if (msg_Hash[index[i]].len > 127) {
                sort_to_Byte(msg_Hash[index[i]].len, result, ind);
                ind += 2;
            } else {
                result[ind] = (byte) msg_Hash[index[i]].len;
                ind += 1;
            }
            arraycopy(msg_Hash[index[i]].data, 0, result, ind, msg_Hash[index[i]].len);
            ind = ind + msg_Hash[index[i]].len;
            msg_Hash[index[i]].len = -1;
            index[i] = 0;
        }
        return ind;
    }
	public static int getByteArrayWithoutReset(TLVArray[] msg_Hash, short[] index, byte[] result) throws Exception {
		int ind = 0;
		for (int i = 0; i < index.length; i++) {
			if (index[i] == 0)
				return ind;
			if (msg_Hash[index[i]].len == -1)
				continue;
			if (index[i] > 127) {
				sort_to_Byte(index[i], result, ind);
				ind += 2;
			} else {
				result[ind] = (byte) index[i];
				ind += 1;
			}
			if (msg_Hash[index[i]].len > 127) {
				sort_to_Byte(msg_Hash[index[i]].len, result, ind);
				ind += 2;
			} else {
				result[ind] = (byte) msg_Hash[index[i]].len;
				ind += 1;
			}
			arraycopy(msg_Hash[index[i]].data, 0, result, ind, msg_Hash[index[i]].len);
			ind = ind + msg_Hash[index[i]].len;
		}
		return ind;
	}

    public static int getByteArraySort(TLVArraySort[] msg_Hash, short[] index, byte[] result) throws Exception {
        int ind = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == 0)
                return ind;
            if (index[i] > 127) {
                sort_to_Byte(index[i], result, ind);
                ind += 2;
            } else {
                result[ind] = (byte) index[i];
                ind += 1;
            }
            if (msg_Hash[index[i]].len > 127) {
                sort_to_Byte(msg_Hash[index[i]].len, result, ind);
                ind += 2;
            } else {
                result[ind] = (byte) msg_Hash[index[i]].len;
                ind += 1;
            }
            arraycopy(msg_Hash[index[i]].data, 0, result, ind, msg_Hash[index[i]].len);
            ind = ind + msg_Hash[index[i]].len;
            msg_Hash[index[i]].len = -1;
            index[i] = 0;
        }
        return ind;
    }


    /*	public static void sort_to_Byte(int var1, byte res[],int i)
            {
                            res[i]= (byte)((var1 & ((short)0x7f)) | 0x80);
                            res[i+1]= (byte)(((short)(var1>>7)) & ((short)0x7f));
            }
            public static short Byte_to_sort(int i, byte res[])
            {
            short var1;
            res[i+1] = (byte)(res[i+1] &(byte)0x7f);
            var1=(short)(128*res[i+1]+res[i]);
            return var1;
            }*/
    public static void sort_to_Byte(int var1, byte res[], int i) {
        res[i + 1] = (byte) ((var1 & ((short) 0x7f)));
        res[i] = (byte) (((short) (var1 >> 7)) & ((short) 0x7f) | 0x80);
    }

    public static short Byte_to_sort(int i, byte res[]) {
        short var1;
        res[i] = (byte) (res[i] & (byte) 0x7f);
        var1 = (short) (128 * res[i] + res[i + 1]);
        return var1;
    }

    public static int getArrayFromByte_KPI(byte ar2[], int length, TLVArray[] msg_Hash, short[] index, int index_count, boolean flag_kpi) {
	try{
        byte[] ll = new byte[2];
        ll[0] = ar2[0];
        ll[1] = ar2[1];
        length = (int) Byte_to_sort(0, ll);
        byte[] ar = new byte[length];
        arraycopy(ar2, 0, ar, 0, length);
        for (int i = 2; i < length; ) {
            short tag;
            short len;
            int bit = (ar[i] >> 7) & 0x01;
            if (bit == 1) {
                tag = Byte_to_sort(i, ar);
                i++;

            } else {
                tag = ar[i];
            }
            bit = (ar[i + 1] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(i + 1, ar);
                i++;

            } else {
                len = ar[i + 1];
            }
            if (flag_kpi) {
		index[index_count] = tag;
		msg_Hash[tag].len = len;
		index_count++;
                arraycopy(ar, i + 2, msg_Hash[tag].data, 0, len);
                i = i + 2 + len;
            } else {
                int type = ar[i + 2];
                int val = ar[i + 3];
                {
		    index[index_count] = tag;
		    msg_Hash[tag].len = len - 2;
                    index_count++;
                    arraycopy(ar, i + 4, msg_Hash[tag].data, 0, len - 2);
                }
                i = i + 2 + len;
            }
        }
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
        return index_count;
    }

    public static int getByteArray_KPI(TLVArray[] msg_Hash, short[] index, byte[] result, ArrayList<Integer> delete) throws Exception {
        int ind = 2;
        boolean flag = true;
        for (int i = 0; i < index.length; i++) {
            flag = true;
            if (delete != null)
                for (int j = 0; j < delete.size(); j++) {
                    if (index[i] == delete.get(j)) {
                        flag = false;
                        break;
                    }
                }
            if (flag) {
                if (msg_Hash[index[i]].len == -1)
                    continue;
                if (index[i] == 0) {
                    sort_to_Byte(ind, result, 0);
                    return ind;
                }

                if (index[i] > 127) {
                    sort_to_Byte(index[i], result, ind);
                    ind += 2;
                } else {
                    result[ind] = (byte) index[i];
                    ind += 1;
                }
                if (msg_Hash[index[i]].len > 127) {
                    sort_to_Byte(msg_Hash[index[i]].len, result, ind);
                    ind += 2;
                } else {
                    result[ind] = (byte) msg_Hash[index[i]].len;
                    ind += 1;
                }
                arraycopy(msg_Hash[index[i]].data, 0, result, ind, msg_Hash[index[i]].len);
                ind = ind + msg_Hash[index[i]].len;
            }
            msg_Hash[index[i]].len = -1;
            index[i] = 0;
        }
        sort_to_Byte(ind, result, 0);
        return ind;
    }

    public static byte nextAction(boolean result, byte oper, int p_length) {

        if (oper == 66)
            return 66;

        if (result) {
            if (oper == 65) {
                if (p_length > 1)
                    return 67;
                else
                    return 83;
            } else
                return 83;
        } else {
            if (oper == 65)
                return 82;
            else {
                if (p_length > 1)
                    return 67;
                else
                    return 82;
            }
        }
    }

    public static int getTypeDetail(int i, int last) {
        int flag = 0;
        Calendar c1 = Calendar.getInstance();
        if (i <= 40) {
            c1.add(Calendar.DAY_OF_MONTH, -(last));
            flag = c1.get(Calendar.DAY_OF_MONTH);

        } else if (i <= 60) {
            c1.add(Calendar.WEEK_OF_YEAR, -(last));
            flag = c1.get(Calendar.WEEK_OF_YEAR);
        } else if (i <= 70) {
            c1.add(Calendar.MONTH, -(last));
            flag = (c1.get(Calendar.MONTH)) + 1;
        } else if (i <= 75) {

            flag = (((c1.get(Calendar.MONTH)) / 3) - last + 1);
            if (flag <= 0)
                flag = 4 - flag;
        } else if (i == 101)
            flag = 101;
        return flag;
    }

    public static int getTypeDetailNew(int i, int count) {
        int flag = 0, tmpd = 0, flagt = 0;
        Calendar c1 = Calendar.getInstance();
        if (i <= 40) {
            tmpd = (c1.get(Calendar.DAY_OF_YEAR));
        } else if (i <= 60) {
            tmpd = (c1.get(Calendar.WEEK_OF_YEAR));
        } else if (i <= 70) {
            tmpd = (c1.get(Calendar.MONTH)) + 1;
        } else if (i <= 75) {
            tmpd = ((c1.get(Calendar.MONTH)) / 3) + 1;
        } else if (i == 101) {
            flagt = 1;
            flag = 101;
        }
        if (flagt == 0) {
            flag = tmpd % count;
            if (flag == 0)
                flag = count;
        }
        return flag;
    }

    public static boolean checkTypeValueNew(int type, int count) {
        if (type == 102)
            return true;
        int new_type_val = getTypeDetailNew(type, count);
        int t_type = type % count;
        if (t_type == 0)
            t_type = count;
        if (t_type == new_type_val || new_type_val == 101)
            return true;
        else
            return false;
    }

    public static boolean checkTypeValue(int type, int type_val, int i) {
        int new_type_val = getTypeDetail(type, i);
        if (new_type_val == type_val && type_val != 101)
            return true;
        else
            return false;
    }

    public static int reset(TLVArray[] msg_Hash, short[] index) {
        int ind = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == 0)
                return ind;
            msg_Hash[index[i]].len = -1;
            index[i] = 0;
        }
        return ind;
    }

    public static int resetSort(TLVArraySort[] msg_Hash, short[] index) {
        int ind = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == 0)
                return ind;
            msg_Hash[index[i]].len = -1;
            index[i] = 0;
        }
        return ind;
    }

    public static int getByte(byte[] p, int[] len2, byte[]... c) {

        //HashByte.arraycopy(getDate(),0,p,0,23);
        int len = 0;//prefix.length;
        for (int j = 0; j < c.length; j++) {
            int ll = c[j].length;
            if (len2[j] != 0)
                ll = len2[j];
            if (ll == -1)
                continue;
            HashByte.arraycopy(c[j], 0, p, len, ll);
            len = len + ll;
            len2[j] = 0;
        }
        //p[len]=10;
        //len++;
        return len;
    }

    public static int getByte(byte[] prefix2, byte[] p, int[] len2, byte[]... c) {

        HashByte.arraycopy(prefix2, 0, p, 0, prefix2.length);
        int len = prefix2.length;
        for (int j = 0; j < c.length; j++) {
            int ll = c[j].length;
            if (len2[j] != 0)
                ll = len2[j];
            if (ll == -1)
                continue;
            HashByte.arraycopy(c[j], 0, p, len, ll);
            len = len + ll;
            len2[j] = 0;
        }
        return len;
    }

    public static byte[] getDate() {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        String CurrentDateTime = formatter.format(c1.getTime());
        return (CurrentDateTime + ":").getBytes();

    }

    public static int getIntArrayFromByte(byte ar[], int length, TLVArray[] msg_Hash, int[] index, int index_count, ArrayList<Integer> camp) {
        int tag;
        short len;
        for (int i = 0; i < length; ) {
            tag = bytearray(ar, i, 4);
            i += 4;
            int bit = (ar[i] >> 7) & 0x01;
            if (bit == 1) {
                len = Byte_to_sort(i, ar);
                i++;

            } else {
                len = ar[i];
            }
            i++;
            int camp_id = camp.indexOf(tag);
            if (camp_id == -1) {
                i = i + len;
                continue;
            }

            arraycopy(ar, i, msg_Hash[camp_id].data, 0, len);
            msg_Hash[camp_id].tag = tag;
            msg_Hash[camp_id].len = len;
            index[index_count] = camp_id;
            i = i + len;
            index_count++;
        }
        return index_count;
    }

    static int bytearray(byte[] arr, int start, int data_type) {
        int j = 0;
        for (int i = 0; i < data_type; i++) {
            j = j | (arr[start + i] & 255) << ((data_type - 1 - i) * 8);
        }
        return j;
    }

    static void bytearraytoint(byte[] arr, int j, int data_type, int start) {
        for (int i = 1; i <= data_type; i++) {
            arr[i + start - 1] = (byte) (j >> ((data_type - i) * 8));
        }

    }

    public static int getByteIntArray(TLVArray[] msg_Hash, int[] index, byte[] result) throws Exception {
        int ind = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == -1)
                return ind;
            if (msg_Hash[index[i]].len == -1)
                continue;
            bytearraytoint(result, msg_Hash[index[i]].tag, 4, ind);
            ind += 4;
            if (msg_Hash[index[i]].len > 127) {
                sort_to_Byte(msg_Hash[index[i]].len, result, ind);
                ind += 2;
            } else {
                result[ind] = (byte) msg_Hash[index[i]].len;
                ind += 1;
            }
            arraycopy(msg_Hash[index[i]].data, 0, result, ind, msg_Hash[index[i]].len);
            ind = ind + msg_Hash[index[i]].len;
            msg_Hash[index[i]].len = -1;
            index[i] = -1;
        }
        return ind;
    }

}