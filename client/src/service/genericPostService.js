import constants from "../data";

const genericPostService = async (endpoint, data) => {
    try {
        const response = await fetch(constants.BASE_URL + endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
            credentials: 'include'
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Unknown error");
        }

        return response.json();
    }
    catch (error) {
        console.error('Error fetching data:', error);
        alert('Error during request:', error.message);
        return { message: error.message };
    }
};


export default genericPostService