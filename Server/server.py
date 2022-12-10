import firebase_admin
import threading
import time
import datetime
from datetime import date
from firebase_admin import credentials
from firebase_admin import db
import requests
cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://testing-6700e-default-rtdb.firebaseio.com/'
})




ref = db.reference('/')
refRequest = db.reference('requestCity').child("Name")
refSaveLocation = db.reference('requestCity').child('lon&lat')
refUserLocation = db.reference('UserLocation')
refUserLocation.set('None')
refError = db.reference('error')
# refAirPollution = db.reference('responseOneCall').child('airPollution')

# refHistory = db.reference('ResponseHistory')
# Moc thoi gian 
print('Server is running')
if (refRequest.get()==None):
    refRequest.set('Can Tho')
def printit():

    threading.Timer(1, printit).start()
    response = requests.get("https://api.openweathermap.org/data/2.5/weather?q="+refRequest.get()+"&appid=1ac90067b919d5a6203d392fb1592877&lang=vi")
    x = response.json()
    try:
        y = x['message']
        if (y == 'city not found'):
            refError.set('city not found')
    except KeyError:
        refError.set('None')
        ref.child("responseName").set(x['name'])
        refSaveLocation.set(x['coord'])
        lat = refSaveLocation.child('lat').get()
        lon = refSaveLocation.child('lon').get()
        responseOneCall = requests.get('https://api.openweathermap.org/data/2.5/onecall?lat='+str(lat)+'&lon='+str(lon)+'&exclude=minutely&units=metric&appid=1ac90067b919d5a6203d392fb1592877&lang=vi')
        responCLKK = requests.get('http://api.openweathermap.org/data/2.5/air_pollution/forecast?lat='+str(lat)+'&lon='+str(lon)+'&appid=1ac90067b919d5a6203d392fb1592877&lang=vi')
        ref.child('responseOneCall').child('thoitiet').set(responseOneCall.json())   
        # refAirPollution.set(responCLKK.json())
        ref.child('responseOneCall').child('khongkhi').set(responCLKK.json())   

    # Response History
    # print('Hello world'+ str(random.randint(1,100)))
    
# printit()

def History():
    threading.Timer(40, History).start()
    # Epoch
    today = round(time.time())
    today1 = date.today()
    _d1 = today1.strftime("%d/%m/%Y")
    _1 = (round(time.mktime(datetime.datetime.strptime(_d1, "%d/%m/%Y").timetuple())))
    _2 = round(_1-86400 )
    _3 = round(_2-86400 )
    _4 = round(_3-86400 )
    _5 = round(_4-86400 )
    _6 = round(_5-86400 )
    _7 = round(_6-86400 )
    _8 = round(_7-86400 )
    _9 = round(_8-86400 )
    _10 = round(_9-86400)
    _11 = round(_10-86400)
    _12 = round(_11-86400 )
    _13 = round(_12-86400 )
    _14 = round(_13-86400 )
    _15 = round(_14-86400)
    _16 = round(_15-86400 )
    _17 = round(_16-86400 )
    _18 = round(_17-86400)
    _19 = round(_18-86400 )
    _20 = round(_19-86400 )
    _21 = round(_20-86400 )
    _22 = round(_21-86400 )
    _23 = round(_22-86400 )
    _24 = round(_23-86400 )
    _25 = round(_24-86400)
    _26 = round(_25-86400 )
    _27 = round(_26-86400 )
    _28 = round(_27-86400 )
    _29 = round(_28-86400 )
    _30 = round(_29-86400 )
   
    # Ngay gio binh thuong
    l = [today,_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27,_28,_29,_30]
    lat = refSaveLocation.child('lat').get()
    value = []
    lon = refSaveLocation.child('lon').get()
    for i in range(0,len(l)-1):
        data = requests.get('http://history.openweathermap.org/data/2.5/history/city?lat='+str(lat)+'&lon='+str(lon)+'&type=hour&start='+str(l[i+1])+'&end='+str(l[i])+'&appid=f6229066195e6bf5ade8b1358680cf69&units=metric')
        value.append(data)
        # ref.child('responseHistory').child(str(i)).set(data.json())
    for i in range(0,len(value)):
        ref.child('responseHistory').child(str(i)).set(value[i].json())

    # print(value)

printit()
History()