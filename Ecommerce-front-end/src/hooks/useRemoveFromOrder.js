import { useMutation } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to edit the cart.
export default function useEditOrder(handleSuccess = () => {}) {
  return useMutation(
    async ({ productId, quantity }) => {
      const response = await fetch(`${API_URL}/removeFromOrder`, {
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        method: "PUT",
        body: JSON.stringify({ productId: Number(productId), quantity: Number(quantity) }),
      });
      return response.json();
    },
    { onSuccess: handleSuccess }
  );
}
