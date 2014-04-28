/* global angular */

var dashboardModule = angular.module('dashboard', []);

dashboardModule.controller('MainController', ['$scope', function($scope) {
  $scope.title = 'Dashboard';
}]);
