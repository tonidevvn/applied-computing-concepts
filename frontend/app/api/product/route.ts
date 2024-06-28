import { NextRequest, NextResponse } from 'next/server';
import { FoodItemType } from '@/app/types/food';
import axios from 'axios';

export async function GET(request: NextRequest) {
  const { searchParams } = new URL(request.url);
  try {
    const response = await axios.get('http://localhost:8080/api/products', { params: searchParams });
    const data: FoodItemType[] = response.data;
    return NextResponse.json(data);
  } catch (error) {
    // Ensure error is typed correctly
    if (axios.isAxiosError(error)) {
      console.error('Error fetching food items:', error.message);
      return NextResponse.json({ message: 'Failed to fetch food items' }, { status: 500 });
    } else {
      console.error('Unexpected error:', error);
      return NextResponse.json({ message: 'An unexpected error occurred' }, { status: 500 });
    }
  }
}
