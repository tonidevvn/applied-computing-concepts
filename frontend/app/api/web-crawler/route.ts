import { NextRequest, NextResponse } from 'next/server';
import axios from 'axios';
import { WebCrawlerProps } from '@/app/types/webcrawler';

export async function GET(request: NextRequest) {
  const { searchParams } = new URL(request.url);

  try {
    const response = await axios.get(`http://localhost:8080/api/web-crawler`, {
      params: searchParams
    });
    const data: WebCrawlerProps = response.data;
    return NextResponse.json(data);
  } catch (error) {
    // Ensure error is typed correctly
    if (axios.isAxiosError(error)) {
      console.error('Error fetching keyword frequency count', error.message);
      return NextResponse.json({ message: 'Failed to fetch frequency count' }, { status: 500 });
    } else {
      console.error('Unexpected error:', error);
      return NextResponse.json({ message: 'An unexpected error occurred' }, { status: 500 });
    }
  }
}
