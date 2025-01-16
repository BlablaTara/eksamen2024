<script setup lang="ts">
import { ref, onMounted } from 'vue';

// Ref til at holde deliveries data
const deliveries = ref<any[]>([]);

// Funktion til at hente deliveries fra backend
const fetchDeliveries = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/deliveries');
    if (response.ok) {
      deliveries.value = await response.json();  // Gem dataene i deliveries
    } else {
      console.error('Fejl ved hentning af deliveries');
    }
  } catch (error) {
    console.error('Der opstod en fejl:', error);
  }
};

// Kald fetchDeliveries når komponenten er monteret
onMounted(fetchDeliveries);
</script>

<template>
  <div>
    <h1>Leveringsliste</h1>
    <ul>
      <!-- Loop gennem deliveries og vis dem -->
      <li v-for="delivery in deliveries" :key="delivery.id">
        <p><strong>Adresse:</strong> {{ delivery.address }}</p>
        <p><strong>Forventet leveringstid:</strong> {{ delivery.expectedDeliveryTime }}</p>
        <p><strong>Drone UUID:</strong> {{ delivery.drone.serialUuid }}</p>
        <p><strong>Pizza:</strong> {{ delivery.pizza.titel }}</p>
      </li>
    </ul>
  </div>
</template>

<style scoped>
/* Du kan tilføje styling her */
</style>
