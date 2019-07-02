 var myApp = angular.module('MyApp', ['ngRoute']);
 myApp.config(function($stateProvider){
        $routeProvider.
                when('/home', {templateUrl: 'home.html',   controller: HomeCtrl}).
                when('/login', {templateUrl: 'login.html',   controller: ListCtrl}).
                otherwise({redirectTo: '/home'});
 });


/* Controllers */

function MainCtrl($scope, Page) {
    console.log(Page);
    $scope.page= Page; 
}

function HomeCtrl($scope, Page) {
    Page.setTitle("Welcome");
}


function ListCtrl($scope, Page, Model) {
    Page.setTitle("Items");
    $scope.items = Model.notes();

}