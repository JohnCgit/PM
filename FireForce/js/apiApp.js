var port = 8081;

function loadRessource(source, method, body) {
    var xhttp = new XMLHttpRequest();
    source = `http://127.0.0.1:${port}`+source;
    xhttp.open(method, source, false);
    xhttp.overrideMimeType("application/json");
    if (body != null) {
        xhttp.setRequestHeader("Content-type", "application/json")
        xhttp.send(body);
    }
    else {
        xhttp.send();
    }
    if (xhttp.status == 200) {
        return xhttp.response;
    }
};

function getFireList(){
    return JSON.parse(loadRessource('http://127.0.0.1:8081/fire',"GET"));
};