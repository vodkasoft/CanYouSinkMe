/* global angular */

var applicationDetailModule = angular.module('applicationDetail', []);

applicationDetailModule.controller('MainController', ['$scope', '$http', '$location', function($scope, $http, $location) {

// Updates the application
$scope.updateApplication = function() {
  // Close general alert
  $('#general-alert .alert').alert('close');
  // Validate form data
  if($scope.application.name !== '') {
    // Remove validation error styles
    $('#application-name-form-group').removeClass('has-error');
    // Prepare new data for application
    var newApplicationData = {};
    newApplicationData.name = $scope.application.name;
    newApplicationData.description = $scope.application.description;
    // Send request
    $http.put('/applications/' + $scope.applicationKey, "application=" + encodeURIComponent(JSON.stringify(newApplicationData)), {
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    })
    .success(function() {
      // Inform successfull
      $('#general-alert').html('<div class="alert alert-success fade in">Successfully update application <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
    }).error(function(data) {
      // Create error message
      var error_message = data.error ? data.error : 'Unable to update the applitacion';
      // Alert error
      $('#general-alert').html('<div class="alert alert-danger fade in">Error: ' + error_message + ' <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
    });
  } else {
    // Add validation error style
    $('#application-name-form-group').addClass('has-error');
  }
};

// Regenerate the client secret for the application
$scope.regenerateClientSecret = function() {
  // Hide modal
  $('#regenerate-client-secret-modal').modal('hide');
  // Send request
  $http.post('/applications/' + $scope.applicationKey + '/client/secret')
  .success(function() {
    // Require refresh
    $scope.refreshRequired = true;
  }).error(function(data) {
    // Create error message
    var error_message = data.error ? data.error : 'Unable to regenerate the client secret';
    // Alert error
    $('#general-alert').html('<div class="alert alert-danger fade in">Error: ' + error_message + ' <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
  });
};

// Regenerate the server response key for the application
$scope.regenerateServerResponseKey = function() {
  // Hide modal
  $('#regenerate-server-response-key-modal').modal('hide');
  // Send request
  $http.post('/applications/' + $scope.applicationKey + '/server/key')
  .success(function() {
    // Require refresh
    $scope.refreshRequired = true;
  }).error(function(data) {
    // Create error message
    var error_message = data.error ? data.error : 'Unable to regenerate the server response key';
    // Alert error
    $('#general-alert').html('<div class="alert alert-danger fade in">Error: ' + error_message + ' <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
  });
};

// Delete the application
$scope.deleteApplication = function() {
  // Hide modal
  $('#delete-application-modal').modal('hide');
  // Send request
  $http.delete('/applications/' + $scope.applicationKey)
  .success(function() {
    // Redirect to applications page
    window.location.href = '/admin/apps';
  }).error(function(data) {
    // Create error message
    var error_message = data.error ? data.error : 'Unable to delete applitacion';
    // Alert error
    $('#general-alert').html('<div class="alert alert-danger fade in">Error: ' + error_message + ' <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
  });
};

// Get the application data
function getApplication() {
  // Send request
  $http.get('/applications/' + $scope.applicationKey)
  .success(function(data) {
    // Save data to scope
    $scope.application = data;
  }).error(function() {
    // Application does not exist
    // Redirect to applications page
    window.location.href = '/admin/apps';
  });
}

// Event handler for when the regenerate client secret modal will appear
$('#regenerate-client-secret-modal').on('show.bs.modal', function() {
  // Hide general alert
  $('#general-alert .alert').alert('close');
});

// Event handler for when the regenerate cliet secret modal has been hidden
$('#regenerate-client-secret-modal').on('hidden.bs.modal', function() {
  // Check if refresh is required
  if($scope.refreshRequired) {
    // Alert successful regeneration of client secret
    $('#general-alert').html('<div class="alert alert-success fade in">Successfully regenerated the client secret <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
    // Get application data again (refresh)
    getApplication();
    // Inform refresh has been handled
    $scope.refreshRequired = false;
  }
});

// Event handler for when the regenerate server response key modal will appear
$('#regenerate-server-response-key-modal').on('show.bs.modal', function() {
  // Hide general alert
  $('#general-alert .alert').alert('close');
});

// Event handler for when the regenerate server response key modal has been hidden
$('#regenerate-server-response-key-modal').on('hidden.bs.modal', function() {
  // Check if refresh is required
  if($scope.refreshRequired) {
    // Alert successful regeneration of server response key
    $('#general-alert').html('<div class="alert alert-success fade in">Successfully regenerated the server response key <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button></div>');
    // Get application data again (refresh)
    getApplication();
    // Inform refresh has been handled
    $scope.refreshRequired = false;
  }});

// Event handler for when the delete application modal will appear
$('#delete-application-modal').on('show.bs.modal', function() {
  // Hide general alert
  $('#general-alert .alert').alert('close');
});

// Get application key from url
$scope.applicationKey = (function (){
  // Get absolute url
  var url = $location.absUrl();
  // Find last path separator
  var lastPathSeparatorIndex = url.lastIndexOf('/');
  // Find index of the query string
  var queryStringIndex = url.indexOf('?');
  // Return application key
  return queryStringIndex > 0 ?
    // Remove query string
    url.substring(lastPathSeparatorIndex + 1, queryStringIndex) :
    // No query string
    url.substring(lastPathSeparatorIndex + 1);
})();

// Initial application data
$scope.application = {};

// No refresh is required at the start
$scope.refreshRequired = false;

// Retrieve application data for initial load
getApplication();

}]);
