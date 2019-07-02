app.controller('loginCtrl', function($scope) {
	// 로그인 체크
	checkLogin();
	
	$(document).ready(function(){
		$("#loginBtn").click(function() { 
			console.log("+++++++++id : "+document.getElementById('username').value);
			var id = document.getElementById('username').value;
			if(id == null) { return;}
			
			var data = {
					id : id,
					pswd : document.getElementById('password').value
			}
			fetch('http://localhost:8080/restful/api/login/', {
			    body: JSON.stringify(data), // must match 'Content-Type' header
			    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
			    credentials: 'same-origin', // include, same-origin, *omit
			    headers: {
			      'user-agent': 'Mozilla/4.0 MDN Example',
			      'content-type': 'application/json'
			    },
			    method: 'POST', // *GET, POST, PUT, DELETE, etc.
			    mode: 'cors', // no-cors, cors, *same-origin
			    redirect: 'follow', // *manual, follow, error
			    referrer: 'no-referrer', // *client, no-referrer
			  })
			  .then(response => response.json()) // parses response to JSON
			  .then(function(data){
				  	console.log(data);
				    if(data.errYn == true){
						alert(data.errMsg);
					}else{
					// 로그인
						sessionStorage.setItem("id", id);
						location.href='/';		
					}
			  }) // JSON from `response.json()` call
			  .catch(error => console.error(error))

			
		 });		
	});
});
app.controller('homeCtrl', function($scope) {
	// 로그인 체크
	checkLogin();
	
	callKakaoApi();

	$(document).ready(function(){
		 // 로그아웃처리
		 $("#loginMenu").click(function() {
			 if(document.getElementById('loginMenu').innerHTML =="Logout") { //로그아웃 메뉴 클릭
				 sessionStorage.removeItem("id");
				location.href='#!/login';			
			 }
		 });
	});
});

app.controller('historyCtrl', function($scope) {
	//console.log("historyCtrl");
	checkLogin();
	
	$(document).ready(function() {
		//console.log("historyCtrl ready");
		//Default Action
		$(".tab_content").hide(); //Hide all content
		$("ul.tabs li:first").addClass("active").show(); //Activate first tab
		$(".tab_content:first").show(); //Show first tab content
		
		//On Click Event
		$("ul.tabs li").click(function() {
			$("ul.tabs li").removeClass("active"); //Remove any "active" class
			$(this).addClass("active"); //Add "active" class to selected tab
			$(".tab_content").hide(); //Hide all tab content
			var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
			$(activeTab).fadeIn(); //Fade in the active content
			return false;
		});
		
		// 최근 검색이력 조회하기
		searchHistoryList();
		
		$("#tab1Btn").click(function() {
			// 최근 검색이력 조회하기
			searchHistoryList();
		});
		$("#tab2Btn").click(function() {
			// 인기검색어 조죄하기
			searchPopularList();
		});
	});
});

function callKakaoApi(){

	// 마커를 담을 배열입니다
	var markers = [];

	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = {
	        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };  

	// 지도를 생성합니다    
	var map = new kakao.maps.Map(mapContainer, mapOption); 

	// 장소 검색 객체를 생성합니다
	var ps = new kakao.maps.services.Places();  

	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({zIndex:1});

	// 키워드로 장소를 검색합니다
	var keyword = document.getElementById('keyword').value;
	if (keyword.replace(/^\s+|\s+$/g, '')) {
		searchPlaces();	
	}

	// 키워드 검색을 요청하는 함수입니다
	function searchPlaces() {
		var keyword = document.getElementById('keyword').value;
	    if (!keyword.replace(/^\s+|\s+$/g, '')) {
	        alert('키워드를 입력해주세요!');
	        return false;
	    }
	    
	    // 키워드를 저장합니다.
	    registerHistory(keyword);
	    
	        
	    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
	    ps.keywordSearch( keyword, placesSearchCB); 
	}

	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
	function placesSearchCB(data, status, pagination) {
		
	    if (status === kakao.maps.services.Status.OK) {

	        // 정상적으로 검색이 완료됐으면
	        // 검색 목록과 마커를 표출합니다
	        displayPlaces(data);

	        // 페이지 번호를 표출합니다
	        displayPagination(pagination);

	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

	        alert('검색 결과가 존재하지 않습니다.');
	        return;

	    } else if (status === kakao.maps.services.Status.ERROR) {

	        alert('검색 결과 중 오류가 발생했습니다.');
	        return;

	    }
	}

	// 검색 결과 목록과 마커를 표출하는 함수입니다
	function displayPlaces(places) {

	    var listEl = document.getElementById('placesList'), 
	    menuEl = document.getElementById('menu_wrap'),
	    fragment = document.createDocumentFragment(), 
	    bounds = new kakao.maps.LatLngBounds(), 
	    listStr = '';
	    
	    // 검색 결과 목록에 추가된 항목들을 제거합니다
	    removeAllChildNods(listEl);

	    // 지도에 표시되고 있는 마커를 제거합니다
	    removeMarker();
	    
	    for ( var i=0; i<places.length; i++ ) {
	    	//console.log(places[i]);
	        // 마커를 생성하고 지도에 표시합니다
	        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
	            marker = addMarker(placePosition, i), 
	            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

	        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
	        // LatLngBounds 객체에 좌표를 추가합니다
	        bounds.extend(placePosition);

	        // 마커와 검색결과 항목에 mouseover 했을때
	        // 해당 장소에 인포윈도우에 장소명을 표시합니다
	        // mouseout 했을 때는 인포윈도우를 닫습니다
	        (function(marker, title, address) {
	            kakao.maps.event.addListener(marker, 'mouseover', function() {
	                displayInfowindow(marker, title,address);
	            });

	            kakao.maps.event.addListener(marker, 'mouseout', function() {
	                infowindow.close();
	            });

	            itemEl.onmouseover =  function () {
	                displayInfowindow(marker, title,address);
	            };

	            itemEl.onmouseout =  function () {
	                infowindow.close();
	            };
	        })(marker, places[i].place_name, places[i].address_name);

	        fragment.appendChild(itemEl);
	    }

	    // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
	    listEl.appendChild(fragment);
	    menuEl.scrollTop = 0;

	    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
	    map.setBounds(bounds);
	}

	// 검색결과 항목을 Element로 반환하는 함수입니다
	function getListItem(index, places) {

	    var el = document.createElement('li'),
	    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
	                '<div class="info">' +
	                '   <h5>' + places.place_name + '</h5>';

	    if (places.road_address_name) {
	        itemStr += '    <span>' + places.road_address_name + '</span>' +
	                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
	    } else {
	        itemStr += '    <span>' +  places.address_name  + '</span>'; 
	    }
	                 
	      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
	                '</div>';           

	    el.innerHTML = itemStr;
	    el.className = 'item';

	    return el;
	}

	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
	function addMarker(position, idx, title) {
	    var imageSrc = 'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
	        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
	        imgOptions =  {
	            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
	            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
	            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
	        },
	        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
	            marker = new kakao.maps.Marker({
	            position: position, // 마커의 위치
	            image: markerImage 
	        });

	    marker.setMap(map); // 지도 위에 마커를 표출합니다
	    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

	    return marker;
	}

	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
	function removeMarker() {
	    for ( var i = 0; i < markers.length; i++ ) {
	        markers[i].setMap(null);
	    }   
	    markers = [];
	}

	// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
	function displayPagination(pagination) {
	    var paginationEl = document.getElementById('pagination'),
	        fragment = document.createDocumentFragment(),
	        i; 

	    // 기존에 추가된 페이지번호를 삭제합니다
	    while (paginationEl.hasChildNodes()) {
	        paginationEl.removeChild (paginationEl.lastChild);
	    }

	    for (i=1; i<=pagination.last; i++) {
	        var el = document.createElement('a');
	        el.href = "#";
	        el.innerHTML = i;

	        if (i===pagination.current) {
	            el.className = 'on';
	        } else {
	            el.onclick = (function(i) {
	                return function() {
	                    pagination.gotoPage(i);
	                }
	            })(i);
	        }

	        fragment.appendChild(el);
	    }
	    paginationEl.appendChild(fragment);
	}

	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
	// 인포윈도우에 장소명을 표시합니다
	function displayInfowindow(marker, title, address) {
		//console.log(address);
	    var content = '<div style="padding:5px;"><b>' + title +'</b>&nbsp'+address +'</div>';

	    infowindow.setContent(content);
	    infowindow.open(map, marker);
	}

	 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
	function removeAllChildNods(el) {   
	    while (el.hasChildNodes()) {
	        el.removeChild (el.lastChild);
	    }
	}
	
	$(document).ready(function(){
		 $("#srchBtn").click(function() { 
			 searchPlaces();
		 });
	});
}
/**
 * 최근 검색어 조회 (id별 조회)
 */
function searchHistoryList(){
	
	if(sessionStorage.getItem("id")){// 로그인 되어있으면
		document.getElementById("tab1").innerHTML="";

		fetch('http://localhost:8080/restful/api/history/'+sessionStorage.getItem("id"))
		  .then(function(response) { //Response는 HTTP Response
		    return response.json(); //Json body 추출
		  })
		  .then(function(myJson) {
		    console.log(myJson);
		    var historyList = myJson.historyList;
		    if(myJson.totalcount> 0){
			    $('#tab1').append("<table class='tab1-table' border=1 ><tr bgcolor='#C0C0C0'><th width=50%>날짜</th><th width=50%>키워드</th></tr></table>");
			    for(var i =0 ; i < myJson.totalcount; i++){
			    	//console.log(i);
			    	//날짜 형식 변환
			    	var date = new Date(historyList[i].srchDt); 
			    	var dateString = date.getFullYear()+"년 "+(date.getMonth()+1)+"월 "+date.getDate()+"일 "
			    					+date.getHours()+"시 "+date.getMinutes()+"분 "+date.getSeconds()+"초";
			    	 $('.tab1-table').append("<tr align='center'><td>"+dateString+"</td><td>"+historyList[i].keyword+"</td></tr>");
			    }
		    }
		  })
		  .catch(error => console.error(error));
	}
	else{// 로그인 되어있지 않으면
		document.getElementById("tab1").innerHTML="<span style='color: #126d9b'>로그인이 필요한 서비스입니다.</span>";
	}
}

/**
 * 인기 검색어 조회
 */
function searchPopularList(){
	document.getElementById("tab2").innerHTML="";
	fetch('http://localhost:8080/restful/api/popular')
	  .then(function(response) { //Response는 HTTP Response
	    return response.json(); //Json body 추출
	  })
	  .then(function(myJson) {
	    //console.log(myJson);    
	    var popularList = myJson.popularList;
	    if(myJson.totalcount > 0){
	    	$('#tab2').append("<table class='tab2-table' border=1 ><tr bgcolor='#C0C0C0'><th width=20%>순위</th><th width=60%>키워드</th><th width=20%>건수</th></tr></table>");
	    
		    for(var i =0 ; i < myJson.totalcount; i++){
		    	//console.log(i);
		    	 //$('.tab2-ul').append("<li style='height: 31px;'>"+(i+1)+"위 :  "+popularList[i].keyword+"("+popularList[i].count+"건)</li>");
		    	 $('.tab2-table').append("<tr align='center'><td>"+(i+1)+"위 </td><td>"+popularList[i].keyword+"</td><td>"+popularList[i].count+"건</td></tr>");
		    }
	    }
	  })
	  .catch(error => console.error(error));
}

function registerHistory(keyword){
	var id = sessionStorage.getItem("id");
	if(!id){
		id ="tmp";
	}
	var data = {
			id : id,
			keyword : keyword,
			srchDt : new Date()
	}
	fetch('http://localhost:8080/restful/api/regHistory', {
	    body: JSON.stringify(data), // must match 'Content-Type' header
	    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
	    credentials: 'same-origin', // include, same-origin, *omit
	    headers: {
	      'user-agent': 'Mozilla/4.0 MDN Example',
	      'content-type': 'application/json'
	    },
	    method: 'POST', // *GET, POST, PUT, DELETE, etc.
	    mode: 'cors', // no-cors, cors, *same-origin
	    redirect: 'follow', // *manual, follow, error
	    referrer: 'no-referrer', // *client, no-referrer
	  })
	  .then(function(response){
		  	console.log(response);		  
	  })

}

function checkLogin(){
	// 로그인 체크
	if(!sessionStorage.getItem("id")) {
		document.getElementById('loginMenu').innerHTML  ="Login";
	}
	else{
		document.getElementById('loginMenu').innerHTML  ="Logout";		
	}
}