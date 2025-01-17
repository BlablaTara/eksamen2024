<script setup lang="ts">
import {ref, onMounted, onUnmounted} from 'vue';

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

const formatTime = (dateTime: string): string => {
  const date = new Date(dateTime);
  const hours = date.getHours().toString().padStart(2, '0'); // Tilføj en foranstillet nul
  const minutes = date.getMinutes().toString().padStart(2, '0'); // Tilføj en foranstillet nul
  return `kl. ${hours}:${minutes}`;
};

const fetchDeliveries = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/deliveries');
    if (response.ok) {
      const data: Delivery[] = await response.json();
      deliveries.value = data
        .map(delivery => ({
          ...delivery,
          drone: delivery.drone || null,
          expectedDeliveryTime: formatTime(delivery.expectedDeliveryTime), // Formatter tidspunktet
        }))
        .sort((a, b) =>
          new Date(a.expectedDeliveryTime).getTime() -
          new Date(b.expectedDeliveryTime).getTime()
        );
    } else {
      console.error('Fejl ved hentning af deliveries');
    }
  } catch (error) {
    console.error('Der opstod en fejl:', error);
  }
};

const scheduleDelivery = async (deliveryId: number) => {
  try {
    const response = await fetch(`http://localhost:8080/api/deliveries/schedule`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        // Vi sender ikke droneId, da backend selv vælger en drone hvis vi ikke sender et
      }),
    });

    if (response.ok) {
      const updatedDelivery = await response.json();
      // Opdater leveringen med den nye drone
      const updatedDeliveries = deliveries.value.map(delivery =>
        delivery.deliveryId === updatedDelivery.deliveryId
          ? { ...delivery, drone: updatedDelivery.drone }
          : delivery
      );
      deliveries.value = updatedDeliveries;
    } else {
      console.error('Fejl ved tildeling af drone');
    }
  } catch (error) {
    console.error('Der opstod en fejl:', error);
  }
};

let intervalId: number | null = null;

onMounted(() => {
  fetchDeliveries();

  // Opdatere hvert 60. sekund
  intervalId = window.setInterval(fetchDeliveries, 60000);
});

onUnmounted(() => {
  // Rydder når komponenten fjernes
  if (intervalId !== null) {
    clearInterval(intervalId);
  }
});

</script>

<template>
  <div>
    <h1>Leveringsliste</h1>
    <div v-if="deliveries.length === 0">
      <p>Der er ingen leveringer at vise.</p>
    </div>
    <ul v-else>
      <li v-for="delivery in deliveries" :key="delivery.deliveryId">
        <p><strong>Pizza:</strong> {{ delivery.pizza.titel }}</p>
        <p><strong>Adresse:</strong> {{ delivery.address }}</p>
        <p><strong>Forventet leveringstid:</strong> {{ delivery.expectedDeliveryTime }}</p>
        <p><strong>Status: </strong>
          <span v-if="!delivery.drone">Mangler Drone</span>
          <span v-else>Afventer Levering</span>
        </p>
        <button v-if="!delivery.drone" @click="scheduleDelivery(delivery.deliveryId)">
          Tildel Drone
        </button>
      </li>
    </ul>
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
  color: #2c3e50;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin: 8px 0;
  background: #f9f9f9;
}

p {
  margin: 4px 0;
}

strong {
  color: #34495e;
}
</style>
