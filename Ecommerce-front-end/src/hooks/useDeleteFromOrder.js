import { useMutation } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to add a product to the cart.
export default function useDeleteFromOrder(handleSuccess = () => {}) {
  return useMutation(
    async ( productId ) => {
      const response = await fetch(`${API_URL}/deleteFromOrder`, {
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        method: "PUT",
        body: JSON.stringify({ productId }),
      });
      return response.json();
    },
    { onSuccess: handleSuccess }
  );
}
