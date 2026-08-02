const API_BASE_URL = "https://graph-route-optimization-20-production.up.railway.app/api";
const routeCache = new Map();
let canvas, ctx;
let particles = [];
let connectionDistance = 110;
let pulseIntensity = 1;

document.addEventListener("DOMContentLoaded", () => {
    loadCities();
    initCanvas();
    initParallaxTilt();
    initRippleEffects();
    initMagneticButtons();
});

// Canvas Cybernetic Particle Constellation Network Visualizer
function initCanvas() {
    canvas = document.getElementById("graphCanvas");
    if (!canvas) return;
    ctx = canvas.getContext("2d");

    resizeCanvas();
    window.addEventListener("resize", resizeCanvas);

    particles = [];
    const particleCount = Math.floor((canvas.width * canvas.height) / 9000);

    for (let i = 0; i < Math.max(25, Math.min(particleCount, 50)); i++) {
        particles.push({
            x: Math.random() * canvas.width,
            y: Math.random() * canvas.height,
            vx: (Math.random() - 0.5) * 0.8,
            vy: (Math.random() - 0.5) * 0.8,
            radius: Math.random() * 2 + 1.2
        });
    }

    animateCanvas();
}

function resizeCanvas() {
    if (!canvas) return;
    canvas.width = canvas.parentElement.clientWidth;
    canvas.height = canvas.parentElement.clientHeight;
}

function animateCanvas() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Fade pulse intensity back to normal smoothly
    if (pulseIntensity > 1) {
        pulseIntensity -= 0.02;
    }

    // Update and draw particles & web connections
    for (let i = 0; i < particles.length; i++) {
        let p = particles[i];
        p.x += p.vx * pulseIntensity;
        p.y += p.vy * pulseIntensity;

        if (p.x < 0 || p.x > canvas.width) p.vx *= -1;
        if (p.y < 0 || p.y > canvas.height) p.vy *= -1;

        ctx.beginPath();
        ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
        ctx.fillStyle = i % 2 === 0 ? "#38bdf8" : "#14b8a6";
        ctx.shadowColor = "#14b8a6";
        ctx.shadowBlur = 6 * pulseIntensity;
        ctx.fill();
        ctx.shadowBlur = 0;

        for (let j = i + 1; j < particles.length; j++) {
            let p2 = particles[j];
            let dx = p.x - p2.x;
            let dy = p.y - p2.y;
            let dist = Math.sqrt(dx * dx + dy * dy);

            if (dist < connectionDistance) {
                ctx.beginPath();
                ctx.moveTo(p.x, p.y);
                ctx.lineTo(p2.x, p2.y);
                let alpha = (1 - (dist / connectionDistance)) * 0.25 * pulseIntensity;
                ctx.strokeStyle = `rgba(20, 184, 166, ${alpha})`;
                ctx.lineWidth = 1;
                ctx.stroke();
            }
        }
    }

    requestAnimationFrame(animateCanvas);
}

function triggerGraphPulse() {
    // Surge particle speed and network brightness on route calculation
    pulseIntensity = 3.2;
    particles.forEach(p => {
        p.vx += (Math.random() - 0.5) * 2;
        p.vy += (Math.random() - 0.5) * 2;
    });
}

// Interactive Magnetic Button Physics Engine
function initMagneticButtons() {
    const magneticElements = document.querySelectorAll(".magnetic-btn");

    magneticElements.forEach(elem => {
        elem.addEventListener("mousemove", (e) => {
            const rect = elem.getBoundingClientRect();
            const h = rect.width;
            const v = rect.height;
            const x = e.clientX - rect.left - h / 2;
            const y = e.clientY - rect.top - v / 2;

            elem.style.transform = `translate(${x * 0.3}px, ${y * 0.3}px)`;
            const content = elem.querySelector(".magnetic-content");
            if (content) {
                content.style.transform = `translate(${x * 0.12}px, ${y * 0.12}px)`;
            }
        });

        elem.addEventListener("mouseleave", () => {
            elem.style.transform = "translate(0px, 0px)";
            const content = elem.querySelector(".magnetic-content");
            if (content) {
                content.style.transform = "translate(0px, 0px)";
            }
        });
    });
}

// Glass Parallax Tilt
function initParallaxTilt() {
    const cards = document.querySelectorAll(".tilt-card");

    cards.forEach(card => {
        card.addEventListener("mousemove", (e) => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const centerX = rect.width / 2;
            const centerY = rect.height / 2;

            const rotateX = ((y - centerY) / centerY) * -2.5;
            const rotateY = ((x - centerX) / centerX) * 2.5;

            card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateY(-3px)`;
        });

        card.addEventListener("mouseleave", () => {
            card.style.transform = `perspective(1000px) rotateX(0deg) rotateY(0deg) translateY(0px)`;
        });
    });
}

// Interactive Ripple Effect
function initRippleEffects() {
    const buttons = document.querySelectorAll(".ripple-btn");
    buttons.forEach(btn => {
        btn.addEventListener("click", function (e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const ripple = document.createElement("span");
            ripple.style.left = `${x}px`;
            ripple.style.top = `${y}px`;
            ripple.className = "ripple";
            this.appendChild(ripple);

            setTimeout(() => ripple.remove(), 600);
        });
    });
}

// Smooth Viewport Switcher
function switchView(targetView) {
    const heroPage = document.getElementById("heroSection");
    const appPage = document.getElementById("appDashboard");
    const navHome = document.getElementById("navHome");
    const navVisualizer = document.getElementById("navVisualizer");

    if (targetView === "app") {
        heroPage.classList.remove("active-view");
        heroPage.classList.add("hidden-view");

        appPage.classList.remove("hidden-view");
        appPage.classList.add("active-view");

        navHome.classList.remove("active");
        navVisualizer.classList.add("active");
    } else {
        appPage.classList.remove("active-view");
        appPage.classList.add("hidden-view");

        heroPage.classList.remove("hidden-view");
        heroPage.classList.add("active-view");

        navVisualizer.classList.remove("active");
        navHome.classList.add("active");
    }
}

async function loadCities() {
    try {
        const response = await fetch(`${API_BASE_URL}/routes/cities`);
        if (!response.ok) throw new Error(`Server status: ${response.status}`);

        const cities = await response.json();
        const sourceSelect = document.getElementById("sourceCity");
        const destSelect = document.getElementById("destinationCity");

        sourceSelect.innerHTML = "";
        destSelect.innerHTML = "";

        if (!cities || cities.length === 0) {
            showError("No cities found in database.");
            return;
        }

        cities.sort().forEach((city) => {
            sourceSelect.add(new Option(city, city));
            destSelect.add(new Option(city, city));
        });

        if (cities.length > 1) {
            destSelect.selectedIndex = 1;
        }

        hideError();
    } catch (error) {
        showError("Error connecting to server. Ensure Spring Boot backend is running on port 8081.");
    }
}

async function findShortestRoute() {
    const source = document.getElementById("sourceCity").value;
    const destination = document.getElementById("destinationCity").value;
    const algorithmSelect = document.getElementById("algorithm");
    const algorithm = algorithmSelect.value;
    const algorithmText = algorithmSelect.options[algorithmSelect.selectedIndex].text;

    if (!source || !destination) {
        showError("Please select both a source and destination city.");
        return;
    }

    if (source === destination) {
        showError("Source and Destination city cannot be identical.");
        return;
    }

    const cacheKey = `${source}-${destination}-${algorithm}`;
    if (routeCache.has(cacheKey)) {
        hideError();
        triggerGraphPulse();
        renderResults(routeCache.get(cacheKey), algorithmText);
        return;
    }

    toggleLoading(true);

    try {
        const response = await fetch(`${API_BASE_URL}/routes/find-route`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ source, destination, algorithm })
        });

        const data = await response.json();

        if (!response.ok || !data.path || data.path.length === 0) {
            showError(data.error || "No valid path found between selected cities.");
            return;
        }

        routeCache.set(cacheKey, data);
        hideError();
        triggerGraphPulse();
        renderResults(data, algorithmText);

    } catch (err) {
        showError("Failed to fetch route results from server.");
    } finally {
        toggleLoading(false);
    }
}

function renderResults(data, algoLabel) {
    document.getElementById("placeholderState").classList.add("hidden");
    document.getElementById("skeletonLoader").classList.add("hidden");
    document.getElementById("resultContainer").classList.remove("hidden");

    document.getElementById("totalDistance").innerText = `${data.totalDistance || 0} km`;
    document.getElementById("visitedNodes").innerText = data.visitedNodes || data.path.length;
    document.getElementById("activeAlgorithm").innerText = algoLabel.split("(")[0].trim();

    const timeline = document.getElementById("routeTimeline");
    timeline.innerHTML = "";

    data.path.forEach((city, index) => {
        const node = document.createElement("div");
        node.className = "timeline-node";
        node.style.animationDelay = `${index * 0.08}s`;

        node.innerHTML = `
            <div class="node-badge">${index + 1}</div>
            <div class="node-name">${city}</div>
        `;
        timeline.appendChild(node);
    });
}

function swapCities() {
    const sourceSelect = document.getElementById("sourceCity");
    const destSelect = document.getElementById("destinationCity");

    const temp = sourceSelect.value;
    sourceSelect.value = destSelect.value;
    destSelect.value = temp;
}

function toggleLoading(isLoading) {
    const btnText = document.getElementById("btnText");
    const btnSpinner = document.getElementById("btnSpinner");
    const btn = document.getElementById("findRouteBtn");
    const placeholder = document.getElementById("placeholderState");
    const skeleton = document.getElementById("skeletonLoader");
    const results = document.getElementById("resultContainer");

    if (isLoading) {
        btnText.classList.add("hidden");
        btnSpinner.classList.remove("hidden");
        btn.disabled = true;

        placeholder.classList.add("hidden");
        results.classList.add("hidden");
        skeleton.classList.remove("hidden");
    } else {
        btnText.classList.remove("hidden");
        btnSpinner.classList.add("hidden");
        btn.disabled = false;
        skeleton.classList.add("hidden");
    }
}

function showError(msg) {
    const errBox = document.getElementById("errorMessage");
    errBox.innerText = msg;
    errBox.style.display = "block";

    document.getElementById("resultContainer").classList.add("hidden");
    document.getElementById("skeletonLoader").classList.add("hidden");
    document.getElementById("placeholderState").classList.remove("hidden");
}

function hideError() {
    const errBox = document.getElementById("errorMessage");
    errBox.style.display = "none";
}