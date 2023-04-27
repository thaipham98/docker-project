import { useMutation } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to add a product to the cart.
export default function useAddToCart(handleSuccess = () => {}) {
  return useMutation(
    async (productId) => {
      const response = await fetch(`${API_URL}/addToOrder`, {
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify({ productId }),
      });
      return response.json();
    },
    {
      onSuccess: handleSuccess,
    }
  );
}
