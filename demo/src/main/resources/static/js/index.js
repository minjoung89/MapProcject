

fetch('http://localhost:8080/restful/api/user/'+'test')
.then(response => response.json())
.then(json => {
   // 받은 json으로 기능 구현
   console.log(json);
//   this.setState({
//     place_name: json.documents.place_name,
//     ...
//   });
});
