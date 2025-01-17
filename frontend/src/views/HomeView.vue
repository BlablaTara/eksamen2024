<script setup lang="ts">
import DeliveryComponent from "@/components/DeliveryComponent.vue";
import DroneComponent from "@/components/DroneComponent.vue";
</script>

<template>
  <main>
    <div class="components-container">
      <DeliveryComponent />
      <DroneComponent />
    </div>
  </main>
</template>

<style scoped>

.components-container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .components-container {
    flex-direction: column;
  }
}
</style>
