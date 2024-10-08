/**
 * This function POSTs to a dummy authentication service fronted by PubSub+.
 * The result of the POST is translated to a boolean and provided back to the caller
 * via the callback function.
 *
 * @param {string} sUsername - Username to use for autentication
 * @param {string} sPassword - Password associated with the username
 *
 * @param {oResultCallback} oResultCallback - callback function to execute on success or failure events.
 */
function authenticate(sUsername, sPassword, oResultCallback) {
    'use strict';

    // Enter serverUrl
    const serverUrl = 'http://localhost:8081/solace/cloud/proxy';
    const headers = {
        'Content-Type': 'application/json; charset=utf-8'
    };
    const jsonBody = {
        username: sUsername,
        password: sPassword
    };

    // Complete fetch parameters; method and headers
    fetch(serverUrl, {
        method: "POST",
        headers: headers,
        body: JSON.stringify(jsonBody)        
    })
        .then(function (response) {
            console.debug(response);
            if (response.ok) {
                oResultCallback(true);
            } else {
                oResultCallback(false);
            }
        });
}