<script setup lang="ts">


import {onMounted, ref} from "vue";
import axios from "axios";

const responseMessage = ref('');


onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/testing');
    responseMessage.value = response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
  }
})
</script>

<template>
  <div>
    <h1>Test Component</h1>
    <p><strong>Message:</strong> {{ responseMessage }}</p>
  </div>
</template>

<style scoped>
h1 {
  color: #42b983
}
</style>
