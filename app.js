document.addEventListener('DOMContentLoaded', () => {
    loadCities();

    const form = document.getElementById('routeForm');
    if (form) {
        form.addEventListener('submit', handleRouteSubmission);
    }
});

async function loadCities() {
    const sourceSelect = document.getElementById('sourceCity');
    const destSelect = document.getElementById('destinationCity');

    try {
        const response = await fetch('/api/cities');
        if (!response.ok) throw new Error('Failed to fetch cities');

        const cities = await response.json();

        sourceSelect.innerHTML = '<option value="">Select Source City</option>';
        destSelect.innerHTML = '<option value="">Select Destination City</option>';

        cities.forEach(city => {
            const cityName = city.name || city.cityName;

            const option1 = document.createElement('option');
            option1.value = cityName;
            option1.textContent = cityName;

            const option2 = document.createElement('option');
            option2.value = cityName;
            option2.textContent = cityName;

            sourceSelect.appendChild(option1);
            destSelect.appendChild(option2);
        });
    } catch (error) {
        console.error('Error loading cities:', error);
        sourceSelect.innerHTML = '<option value="">Error loading cities</option>';
        destSelect.innerHTML = '<option value="">Error loading cities</option>';
    }
}

async function handleRouteSubmission(event) {
    event.preventDefault();

    const source = document.getElementById('sourceCity').value;
    const destination = document.getElementById('destinationCity').value;
    const errorElement = document.getElementById('errorMessage');
    const resultElement = document.getElementById('resultDisplay');

    if (!source || !destination) {
        errorElement.textContent = 'Please select both a source and destination city.';
        errorElement.style.display = 'block';
        return;
    }

    if (source === destination) {
        errorElement.textContent = 'Source and destination cities must be different.';
        errorElement.style.display = 'block';
        return;
    }

    errorElement.style.display = 'none';

    try {
        const response = await fetch(`/api/routes/shortest-path?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}`);
        const data = await response.json();

        if (!response.ok) {
            errorElement.textContent = data.error || 'Failed to calculate shortest route.';
            errorElement.style.display = 'block';
            return;
        }

        resultElement.innerHTML = `
            <h3>Optimized Route</h3>
            <p><strong>Path:</strong> ${data.path.join(' ➔ ')}</p>
            <p><strong>Total Distance:</strong> ${data.distance} km</p>
        `;
    } catch (error) {
        console.error('Error finding route:', error);
        errorElement.textContent = 'Server connection error. Please try again.';
        errorElement.style.display = 'block';
    }
}