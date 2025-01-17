<script setup lang="ts">
import { ref, onMounted } from 'vue';


type Drone = {
  serialUuid: string;
  droneStatus: string;
  stationId: string | null;
};

const drones = ref<Drone[]>([]);

// Funktion til at hente alle droner fra backend
const fetchDrones = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/drones');
    if (response.ok) {
      const data: Drone[] = await response.json();
      drones.value = data;
    } else {
      console.error('Fejl ved hentning af droner');
    }
  } catch (error) {
    console.error('Der opstod en fejl:', error);
  }
};

// Funktion til at oprette en ny drone via backend
const createDrone = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/drones/add', {
      method: 'POST',
    });

    if (response.ok) {
      const newDrone: Drone = await response.json();
      drones.value.push(newDrone); // Tilføj den nye drone til listen
      console.log('Drone oprettet:', newDrone);
    } else {
      console.error('Fejl ved oprettelse af drone');
    }
  } catch (error) {
    console.error('Der opstod en fejl:', error);
  }
};

// Hent droner ved montering af komponenten
onMounted(() => {
  fetchDrones();
});
</script>

<template>
  <div class="droneList">
    <h1>Droneliste</h1>

    <!-- Knappen til at oprette en ny drone -->
    <button @click="createDrone">Opret Drone</button>

    <!-- Vis dronelisten -->
    <div v-if="drones.length === 0">
      <p>Ingen droner tilgængelige.</p>
    </div>
    <ul v-else>
      <li v-for="drone in drones" :key="drone.serialUuid">
        <p><strong>Drone ID:</strong> {{ drone.serialUuid }}</p>
        <p><strong>Status:</strong> {{ drone.droneStatus }}</p>
        <p><strong>Station:</strong> {{ drone.stationId || 'Ingen station' }}</p>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.droneList {
  width: 40%;
  margin: 0 auto;
  padding: 1rem;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px 30px; /* Øger padding for at gøre elementerne bredere */
  margin: 10px 0; /* Øger margin mellem elementerne */
  background: #f9f9f9;
  width: 100%; /* Gør listeelementerne bredere */
  box-sizing: border-box; /* Sørger for at padding ikke påvirker bredden */
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 20px; /* Giv lidt ekstra plads under overskriften */
}

button {
  font-size: 16px;
  padding: 12px 24px;
  border: 2px solid #82c482; /* Farven ændret til grønt som i din tidligere kode */
  background-color: #fff;
  color: green;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

button:hover {
  background-color: #82c482;
  color: white;
}

button.inactive {
  font-size: 16px;
  padding: 12px 24px;
  border: 2px solid #7c7a7a;
  background-color: #f0f0f0;
  color: #7c7a7a;
  border-radius: 8px;
  cursor: not-allowed;
}

button.inactive:hover {
  background-color: #f0f0f0;
  color: #7c7a7a;
}

</style>
