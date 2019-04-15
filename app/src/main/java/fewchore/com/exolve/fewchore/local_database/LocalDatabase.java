package fewchore.com.exolve.fewchore.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fewchore.com.exolve.fewchore.model.BankModel;
import fewchore.com.exolve.fewchore.model.Banks;
import fewchore.com.exolve.fewchore.model.CardModel;


public class LocalDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="Fewchore";
    private static final int DATABASE_VERSION = 5;

    private static final String CARD_LIST = "card_list";
    private static final String CARD_ID = "card_id";
    private static final String CARD_NO = "card_no";
    private static final String CARD_EXPIRY_DATE = "date";
    private static final String CARD_ASSIGNED_ID = "cvv";
    private static final String CARD_TYPE = "card_type";

    private static final String AUTH_CODE = "auth_code";
    private static final String ALL_CARD= "CREATE TABLE "+CARD_LIST+
            " ("+ CARD_ID + " TEXT, "+CARD_NO + " INTEGER, "+CARD_EXPIRY_DATE +" INTEGER, "
            +CARD_TYPE +" TEXT, "+AUTH_CODE +" TEXT, "+ CARD_ASSIGNED_ID +" INTEGER );";


    private static final String BANK_ID = "id";
    private static final String BANK_NAME = "bank_name";
    private static final String BANK_ACCT_NO = "acct_no";
    private static final String ACCT_TYPE = "acct_type";
    private static final String ACCT_LIST = "acct_list";
    private static final String BANK_LIST = "bank_list";

    private static final String BVN = "bvn";
    private static final String ACCT_NAME = "acct_name";
    private static final String ALL_ACCOUNTS = "CREATE TABLE "+ ACCT_LIST +
            " ("+ BANK_ID + " INTEGER PRIMARY KEY, "+BANK_NAME + " TEXT, "+ACCT_NAME + " TEXT, "+BVN + " TEXT, "+BANK_ACCT_NO +" TEXT, "+ACCT_TYPE +" TEXT );";

    private static final String ALL_BANKS= "CREATE TABLE "+ BANK_LIST +" ("+ BANK_ID + " TEXT, "+BANK_NAME + " TEXT );";



    private Context context;
    private String TAG ="Local_Database";


    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(ALL_CARD);
        sqLiteDatabase.execSQL(ALL_ACCOUNTS);
        sqLiteDatabase.execSQL(ALL_BANKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CARD_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ACCT_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ BANK_LIST);
        onCreate(sqLiteDatabase);
    }

    public void clearDB(){
        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CARD_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ACCT_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ BANK_LIST);
        onCreate(sqLiteDatabase);
    }
    public void insertCard(List<CardModel> cardModelList) {
        SQLiteDatabase db = null;
        ContentValues contentValues;
        if (cardModelList != null && (!cardModelList.isEmpty())){
            db = this.getWritableDatabase();
            for (CardModel cardModel : cardModelList){
                if (!cardExists(cardModel.getCardNo(), db)){
                    contentValues = new ContentValues();
                    contentValues.put(CARD_NO,cardModel.getCardNo());
                    contentValues.put(CARD_EXPIRY_DATE,cardModel.getCardExpiry());
                    contentValues.put(CARD_ASSIGNED_ID,cardModel.getCardId());
                    contentValues.put(CARD_TYPE, cardModel.getCardType());
                    contentValues.put(AUTH_CODE, cardModel.getAuthCode());
                    db.insert(CARD_LIST, null, contentValues);
                }
            }
        }
        if (db != null)
            db.close();
        Log.i("Card", "Card inserted");
    }

    private boolean cardExists(String cardNo, SQLiteDatabase db) {
        boolean status = false;
        Cursor cr = db.query(CARD_LIST, new String[]{CARD_NO},
                CARD_NO + " = ?", new String[]{cardNo}, null, null, null, null);
        if (cr != null && (cr.getCount() > 0)){
            status = true;
        }
        assert  cr != null;
        cr.close();
        return status;
    }


    public void insertAccts(List<BankModel> bankModelList) {
        SQLiteDatabase db = null;
        ContentValues contentValues;
        if (bankModelList != null && (!bankModelList.isEmpty())){
            db = this.getWritableDatabase();
            for (BankModel bankModel : bankModelList){
                if (!acctExists(bankModel.getAcctNo(), db)){
                    contentValues = new ContentValues();
                    contentValues.put(BANK_NAME,bankModel.getBankName());
                    contentValues.put(BANK_ACCT_NO,bankModel.getAcctNo());
                    contentValues.put(ACCT_TYPE,bankModel.getAcctType());
                    contentValues.put(ACCT_NAME, bankModel.getAcctName());
                    contentValues.put(BVN, bankModel.getBvn());

                    db.insert(ACCT_LIST, null, contentValues);
                }
            }
        }
        if (db != null)
            db.close();
        Log.i("Card", "Card inserted");
    }

    private boolean acctExists(String acctNo, SQLiteDatabase db) {
        boolean status = false;
        Cursor cr = db.query(ACCT_LIST, new String[]{BANK_ACCT_NO},
                BANK_ACCT_NO + " = ?", new String[]{acctNo}, null, null, null, null);
        if (cr != null && (cr.getCount() > 0)){
            status = true;
        }
        assert  cr != null;
        cr.close();
        return status;
    }

    public void insertBank(List<Banks> banksList) {
        SQLiteDatabase db = null;
        ContentValues contentValues;
        if (banksList != null && (!banksList.isEmpty())){
            db = this.getWritableDatabase();
            for (Banks bankModel : banksList){
                if (!bankExists(bankModel.getBankId(), db)){
                    contentValues = new ContentValues();
                    contentValues.put(BANK_ID,bankModel.getBankName());
                    contentValues.put(BANK_NAME,bankModel.getBankName());

                    db.insert(BANK_LIST, null, contentValues);
                }
            }
        }
        if (db != null)
            db.close();
        Log.i("Card", "Card inserted");
    }

    private boolean bankExists(String bankId, SQLiteDatabase db) {
        boolean status = false;
        Cursor cr = db.query(BANK_LIST, new String[]{BANK_ID},
                BANK_ID + " = ?", new String[]{bankId}, null, null, null, null);
        if (cr != null && (cr.getCount() > 0)){
            status = true;
        }
        assert  cr != null;
        cr.close();
        return status;
    }


    private String [] card = {CARD_NO,CARD_EXPIRY_DATE, CARD_ASSIGNED_ID, CARD_TYPE,AUTH_CODE};

    public List<CardModel> getAllCard() {
        List<CardModel> cardModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        CardModel cardModel;

        cursor = db.query(CARD_LIST, card,null, null,null,null,null, null);

        if (cursor != null && (cursor.getCount()>0)){
            cursor.moveToFirst();
            do{
                cardModel = new CardModel();

                cardModel.setCardNo(cursor.getString(0));
                cardModel.setCardExpiry(cursor.getString(1));
                cardModel.setCardId(cursor.getString(2));
                cardModel.setCardType(cursor.getString(3));
                cardModel.setAuthCode(cursor.getString(4));
                cardModelList.add(cardModel);
            }while (cursor.moveToNext());
        }assert cursor != null;
        cursor.close();
        db.close();
        return cardModelList;

    }

    private String [] acct = {BANK_NAME,BANK_ACCT_NO, ACCT_TYPE, ACCT_NAME, BVN};

    public ArrayList<BankModel> getAllAcct() {
        ArrayList<BankModel> bankModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        BankModel bankModel;

        cursor = db.query(ACCT_LIST, acct,null, null,null,null,null, null);

        if (cursor != null && (cursor.getCount()>0)){
            cursor.moveToFirst();
            do{
                bankModel = new BankModel();

                bankModel.setBankName(cursor.getString(0));
                bankModel.setAcctNo(cursor.getString(1));
                bankModel.setAcctType(cursor.getString(2));
                bankModel.setAcctName(cursor.getString(3));
                bankModel.setBvn(cursor.getString(4));
                bankModelList.add(bankModel);
            }while (cursor.moveToNext());
        }assert cursor != null;
        cursor.close();
        db.close();
        return bankModelList;

    }
    public void clearCard(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CARD_LIST, null, null);

    }
    private String [] bank = {BANK_ID,BANK_NAME};

    public ArrayList<Banks> getAllBank() {
        ArrayList<Banks> bankModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        Banks bankModel;

        cursor = db.query(BANK_LIST, bank,null, null,null,null,null, null);

        if (cursor != null && (cursor.getCount()>0)){
            cursor.moveToFirst();
            do{
                bankModel = new Banks();

                bankModel.setBankId(cursor.getString(0));
                bankModel.setBankName(cursor.getString(1));
                bankModelList.add(bankModel);
            }while (cursor.moveToNext());
        }assert cursor != null;
        cursor.close();
        db.close();
        return bankModelList;

    }


}
