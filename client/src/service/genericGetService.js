import constants from "../data";

async function getApiData(endpoint, options = {}) {
    const defaultHeaders = {
        'Content-Type': 'application/json'
    };

    const headers = { ...defaultHeaders, ...options.headers };

    try {
        const response = await fetch(constants.BASE_URL + endpoint, {
            method: 'GET',
            headers: headers,
            credentials: "include",
            ...options
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Unknown error");
        }

        const data = await response.json();
        return data;

    } catch (error) {
        console.error('Error fetching data:', error);
        alert('Error during request:', error.message);
        return { message: error.message };
    }
}

export default getApiData

// Example usage

// const url = 'https://api.example.com/data';
// const options = {
//     headers: {
//         'Authorization': 'Bearer your-token'
//     }
// };

// getApiData(url, options)
//     .then(data => {
//         console.log('Data:', data);
//     })
//     .catch(error => {
//         console.error('Error:', error);
//     });