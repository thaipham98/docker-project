import { useQuery } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to get the product.
export default function useSearchProduct(id) {
  return useQuery(["product", id], async () => {
    const response = await fetch(`${API_URL}/viewProduct/${id}`);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  });
}
