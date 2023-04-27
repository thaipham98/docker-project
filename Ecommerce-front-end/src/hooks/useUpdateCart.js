import { useMutation } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to update the cart.
export default function useUpdateCart(handleSuccess = () => {}) {
  return useMutation(
    async ({ id, quantity }) => {
      const response = await fetch(`${API_URL}/editOrder`, {
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify({ id: Number(id), quantity: Number(quantity) }),
      });
      return response.json();
    },
    { onSuccess: handleSuccess }
  );
}
