import play.api.libs.json.{JsValue, Json}

val json: JsValue = Json.parse("""
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
""")

val jsonString  = Json.stringify(json)
val lat = (json \ "location" \ "lat").get
val bigwig = (json \ "residents" \ 1).get
val names = json \\ "name"
val bigwigs = json("residents")