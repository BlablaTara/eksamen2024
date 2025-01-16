<script setup lang="ts">
import { ref, onMounted } from 'vue';

type Drone = {
  serialUuid: string | null;
};

type Pizza = {
  titel: string;
};

type Delivery = {
  deliveryId: number;
  address: string;
  expectedDeliveryTime: string;
  drone: Drone | null; // Drone kan være null
  pizza: Pizza;
};


const deliveries = ref<Delivery[]>([]);


const fetchDeliveries = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/deliveries');
    if (response.ok) {
      const data: Delivery[] = await response.json();
      deliveries.value = data.map(delivery => ({
      ...delivery,
        drone: delivery.drone || null,
      }));
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
      <li v-for="delivery in deliveries" :key="delivery.deliveryId">
        <p><strong>Adresse:</strong> {{ delivery.address }}</p>
        <p><strong>Forventet leveringstid:</strong> {{ delivery.expectedDeliveryTime }}</p>
        <p><strong>Drone UUID:</strong>
          <!-- Hvis drone findes, vis dens UUID, ellers vis 'Ingen drone' -->
          {{ delivery.drone ? 'Har Drone' : 'Mangler Drone' }}
        </p>
        <p><strong>Pizza:</strong> {{ delivery.pizza.titel }}</p>
      </li>
    </ul>
  </div>
</template>

<style scoped>
/* Du kan tilføje styling her */
</style>
