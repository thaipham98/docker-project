import { useQuery } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to get the cart.
export default function useCart() {
  return useQuery(["cart"], async () => {
    const response = await fetch(`${API_URL}/showOrder`);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  });
}
