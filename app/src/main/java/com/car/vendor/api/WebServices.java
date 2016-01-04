package com.car.vendor.api;

/**
 * Created by AMAN on 25-10-2015.
 */
public class WebServices {

    public static final String baseUrl = "http://45.55.245.79:81/projects/expensemanager/carservice/api/";

    public static final String vendor_signUp = baseUrl +  "Test/vendor_register";

    /*
    Request
    {
    "name":"name",
    "email":"email",
    "password":"password",
    "mobile":"mobile"
    } */


    public static final String vendor_signIn = baseUrl  + "Test/vendor_login_action";
    /*
    Request
    {
    "email":"niha@niha.niha",
    "password":"niha"
    }
*/

    public static final String vendor_business_profile_add=baseUrl+ "Test/business_profile_add";

   /* {"vendor_id":"6",
            "name":"Nihas Carz Care",
            "services":["car wash","polish"],
        "address":"BtmLayout",
            "city":"Bangalore",
            "state":"Karnataka",
            "country":"India",
            "pincode":"500530",
            "contact":"9999955555",
            "from":"9 am",
            "to":"10pm"
    }*/




    public static final String getVendorsbyLocation(String location){
        return baseUrl+"Test/search_by_location?location="+location;
    }

    public static final String user_book_service=baseUrl+ "Test/book_service";

    /*{
    "user_id":"4",
    "service_id":"13",
    "vendor_id":"6",
    "services":["car wash","polish"],
        "address":"bbb",
            "conv_time":"6 pm to 7pm",
            "mobile":"9999999999"
    }*/







    //  ,.....................................  USER MODULE......................................................

    public static final String user_signUp = baseUrl +  "Test/user_register";

    /*{
        "name":"nihas_user",
            "email":"nihas@user.nihas",
            "password":"nihas",
            "mobile":"1123456789"
    }*/

   // response {"status":"success","message":"Registered Successully","user_id":"4","name":"nihas_user","email":"nihas@user.nihas"}

    public static final String user_signIn = baseUrl  + "Test/user_login_action";
    /*
    Request
    {
    "email":"niha@niha.niha",
    "password":"niha"
    }
*/

    //response   {"status":"success","user_id":"4","name":"nihas_user","email":"nihas@user.nihas"}



    public static final String getUserBookings(int user_id){
       return baseUrl  + "Test/user_bookings?user_id="+user_id;
    }

    public static final String vendor_pickup = baseUrl  + "api/Test/pickup_order";

  /*  {
        "user_id":"4",
            "booking_id":"13",
            "employee":"sasi",
            "mobile":"9999999999",
            "start_time":"7 pm"
    }*/


    public static final String vendor_picked_successfully = baseUrl  + "api/Test/pickup_complete";

    /*{
        "user_id":"4",
            "booking_id":"6",
            "arrival_time":"9 pm",
            "service_status":"0"
    }*/

    public static final String vendor_service_steps = baseUrl  + "api/Test/service_steps";

   /* {
        "user_id":"4",
            "booking_id":"6",
            "service":"car wash",
            "status":"processing"
    }*/

    public static final String vendor_service_steps_update = baseUrl  + "api/Test/service_steps";

    /*{
        "user_id":"4",
            "booking_id":"6",
            "service":"polish",
            "status":"processing"
    }*/













    public static final String forgotPass = baseUrl  + "merchants/forgotPass";
    /*
    Request
    {
        "email":"aa@aa.com"
    }*/

    public static final String category = baseUrl  + "establishmenttype/category";

    public static final String typeDetails = baseUrl  + "establishmenttype/typeDetails";

    /*
     Request
    {
     "type":
                [
                      "Restaurant",
                       "Halls"
                ]
}
*/

    public static final String cuisine = baseUrl  + "establishmenttype/cuisine";

    public static final String ambiance = baseUrl  + "establishmenttype/ambiance";

    public static final String allBuisnessProf = baseUrl  + "merchants/allBuisnessProf";
   /*
   Request
   {
        "userId":"563bcf4c1d76b3ac42d36fd9",
            "offset":"0",
            "limit":"2"
    }
*/

 /*
  Response
  {
        "notice":{
        "allProfiles":[{
            "Business_Name":"Sample",
                    "Cover_Image":"/FFFABRRRQAUUUUAFFFFAB\nRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAH/9k=\n",
                    "Location_Name":"Domlur Bangalore",
                    "category":"Restaurant",
                    "profileId":"SampleDomlur BangaloreRestaurant"
        }
        ]
    }
    }*/


    public static final String tempBuisnessProf = baseUrl  + "merchants/tempBuisnessProf";

    public static final String saveDeal = baseUrl  + "merchants/saveDeal";

    public static final String saveBuisnessProf = baseUrl  + "merchants/saveBuisnessProf";

    public static final String getDeals = baseUrl  + "merchants/getDeals";

    public static final String deleteBuisnessProf = baseUrl  + "merchants/deleteBuisnessProf";

    public static final String deleteDeal = baseUrl  + "merchants/deleteDeal";

    public static final String getOneBuisnessProf = baseUrl  + "merchants/getOneBuisnessProf";

    public static final String getOneDeal = baseUrl  + "merchants/getOneDeal";

    public static final String getUserProfile = baseUrl  + "merchants/getUserProfile";

    /*{  "notice":
        {    "profile":
            {      "firstName": "aaa",
                    "lastName": "aaa",
                    "establishmentName": "aa",
                    "email": "a@b.com",
                    "mobile": "1234231232",
                    "userImage": null
            }
        },
        "status": "Success"
    }*/

    public static final String updateUserProfile = baseUrl  + "merchants/updateUserProfile";

    /*{
            "userId":"563bcf4c1d76b3ac42d36fd9",
            "firstName": "aaa",
            "lastName": "aaa",
            "establishmentName": "Airtel",
            "email": "a@b.com",
            "mobile": "1234231232"
    }*/

    public static final String updateBuisnessProf = baseUrl  + "merchants/updateBuisnessProf";

    public static final String updateDeal = baseUrl  + "merchants/updateDeal";


    /*
    Request
    {
        "userId":"563bcf4c1d76b3ac42d36fd9",
            "offset":"0",
            "limit":"2"
    }
     */


    //For Socket

}

