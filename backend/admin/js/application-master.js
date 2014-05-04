// Copyright 2014 Vodkasoft
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* global angular */
/* global console */

// Module
var applicationMasterModule = angular.module('applicationMaster', []);

// Controller
applicationMasterModule.controller('MainController', ['$scope', '$http', function($scope, $http) {

  // Retrieve and display all applications
  $scope.getApplications = function() {
    // Send request
    $http.get('/applications')
    .success(function(data) {
      var apps = data.applications;
      // Convert dates from UTC to local time
      for (var i = apps.length - 1; i >= 0; i--) {
        apps[i].registered = new Date(Date.parse(apps[i].registered));
      }
      $scope.applications = apps;
    }).error(function() {
      // Alert error
      $('#general-alert .alert').alert('close');
      $('#general-alert').html('<div class="alert alert-danger fade in">Error: Unable to get registered applications <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
    });
  };

  // Set ordering of registered applications table
  $scope.setPredicate = function(newPredicate) {
    $scope.predicate = newPredicate;
    $scope.reverse=!$scope.reverse;
  };

  // Create a new application
  $scope.createApplication = function() {
    // Close modal alerts
    $('#creation-alert .alert').alert('close');
    // Validate form data
    if($scope.newApp.name !== '') {
      // Remove validation error styles
      $('#new-application-name-form-group').removeClass('has-error');
      // Send request
      $http.post('/applications', {application: $scope.newApp}, {
        headers: {'Content-Type': 'application/json'}
      })
      .success(function() {
        // Inform application was created, require refresh
        $scope.applicationCreated = true;
        // Hide modal
        $('#create-application-modal').modal('hide');
      }).error(function(data) {
        console.log(data);
        // Create error message
        var error_message = data.error ? data.error : 'Unable to create the applitacion';
        // Alert error
        $('#creation-alert').html('<div class="alert alert-danger fade in">Error: ' + error_message + ' <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
      });
    } else {
      // Add validation error styles
      $('#new-application-name-form-group').addClass('has-error');
    }
  };

  // Event handler for when the create application modal will appear
  $('#create-application-modal').on('show.bs.modal', function() {
    // Close general alerts
    $('#general-alert .alert').alert('close');
  });

  // Event handler for when the create application modal has appeared
  $('#create-application-modal').on('shown.bs.modal', function() {
    // Set focus to first form field
    $('#new-application-name').focus();
  });

  // Event handler for when the create application modal has been hidden
  $('#create-application-modal').on('hidden.bs.modal', function() {
    // Close modal alerts
    $('#creation-alert .alert').alert('close');
    // Remove validation error classes
    $('#new-application-name-form-group').removeClass('has-error');
    // Reset form values
    $('#new-application-name').val('');
    $('#new-application-description').val('');
    // Check if refresh is required
    if($scope.applicationCreated) {
      // Alert successful creation
      $('#general-alert').html('<div class="alert alert-success fade in">Successfully created application <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
      // Get applications again (refresh)
      $scope.getApplications();
      // Inform refresh has been handled
      $scope.applicationCreated = false;
    }
  });

  // Initial sorting
  $scope.predicate = '-RegistrationDate';

  // Initial values for new application
  $scope.newApp = {name: '', description: ''};

  // No refresh required by default
  $scope.applicationCreated = false;

  // Retrieve applications for initial load
  $scope.getApplications();

}]);
