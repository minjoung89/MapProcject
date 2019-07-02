var app = angular.module("myApp", ["ngRoute"]);
app.config(function($routeProvider) {
  $routeProvider
  .when("/login", {
    templateUrl : "login.html",
     controller: "loginCtrl"
  })
  .when("/",{
	 templateUrl : "home.html",
	 controller: "homeCtrl"
  })
  .when("/history",{
	 templateUrl : "history.html",
	 controller: "historyCtrl"
  });
});
